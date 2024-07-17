import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router';
import { useStore, Info } from './store';
import { useStoreSelected, Selected } from 'Pages/LibrarySetting';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import { StudentSeatReservationMessage } from 'Plugins/StudentAPI/StudentSeatReservationMessage';

interface SeatReservationModalProps {
    isOpen: boolean;
    library: string;
    floor: string;
    area: string;
    seat: string;
    occupied: boolean;
    onClose: () => void;
}

const modalStyle: React.CSSProperties = {
    position: 'fixed',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    backgroundColor: 'white',
    padding: '20px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    zIndex: 1000,
    textAlign: 'center',
};

const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    zIndex: 999,
};

const buttonStyle: React.CSSProperties = {
    padding: '10px 20px',
    margin: '10px',
    border: '1px solid black',
    cursor: 'pointer',
};

const cancelButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    backgroundColor: 'white',
};

const confirmButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    backgroundColor: '#20B2AA',
};

const successButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    backgroundColor: '#32CD32',
};

const reportButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    backgroundColor: '#FFA500',
};

export const SeatReservationModal: React.FC<SeatReservationModalProps> = ({ isOpen, library, floor, area, seat, occupied, onClose }) => {
    const info = useStore((state) => state.info);
    const setInfo = useStore((state) => state.setInfo);

    const setSelected = useStoreSelected((state) => state.setLibrary);
    const history = useHistory();
    const [success, setSuccess] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (success) {
            setTimeout(() => {
                setSuccess(null);
                onClose();
            }, 4000);
        }
    }, [success, onClose]);

    const handleConfirm = () => {
        const updatedInfo: Info = { ...info, floor, sectionNumber: area, seatNumber: seat };
        const message = new StudentSeatReservationMessage(info.token, floor, area, seat);
        console.log("Sending reservation message:", message);
        sendPostRequest(message, setError, (response) => {
            console.log('Reservation response:', response);
            setSuccess("Reservation successful!"); // Ensure success message is set
            setInfo(updatedInfo);
        });
    };

    const handleCancel = () => {
        onClose();
    };

    const handleReportIssue = () => {
        const selected: Selected = { library, floor, area, seat };
        setSelected(selected);
        history.push('/Report');
    };

    if (!isOpen) return null;

    let message = '';
    let showConfirmButton = true;

    if (info.volunteerStatus === "true") {
        message = 'Please complete your volunteer work before reserving a seat.';
        showConfirmButton = false;
    } else if (occupied) {
        message = 'This seat is already occupied.';
        showConfirmButton = false;
    } else if (!success && info.floor !== "0" && info.sectionNumber !== "0" && info.seatNumber !== "0") {
        message = 'You have already reserved a seat. Please do not reserve another one.';
        showConfirmButton = false;
    } else if (!success) {
        message = `You are reserving seat ${floor==='0'?'G':`F${floor}`}${area}${seat.padStart(3,'0')} in ${area} area on floor ${floor} of ${library}.`;
    }

    return (
        <>
            <div style={overlayStyle} onClick={onClose} />
            <div style={modalStyle}>
                <h2>Seat Reservation</h2>
                {!success ? (
                    <>
                        <p>{message}</p>
                        <button
                            style={cancelButtonStyle}
                            onClick={handleCancel}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'lightgrey')}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = 'white')}
                        >
                            Cancel
                        </button>
                        {showConfirmButton && (
                            <button
                                style={confirmButtonStyle}
                                onClick={handleConfirm}
                                onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'lightgreen')}
                                onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#20B2AA')}
                            >
                                Confirm
                            </button>
                        )}
                        <button
                            style={reportButtonStyle}
                            onClick={handleReportIssue}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'darkorange')}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#FFA500')}
                        >
                            Report Issue
                        </button>
                    </>
                ) : (
                    <>
                        <p>{info.userName}, you have successfully reserved seat {floor==='0'?'G':`F${floor}`}{area}{seat.padStart(3,'0')} in {area} area on floor {floor} of {library} library.</p>
                        <button
                            style={successButtonStyle}
                            onClick={()=>setTimeout(()=>handleCancel,2500)}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'lightgreen')}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#32CD32')}
                        >
                            OK
                        </button>
                    </>
                )}
                <ErrorModal message={error} onClose={onClose} />
            </div>
        </>
    );
};
