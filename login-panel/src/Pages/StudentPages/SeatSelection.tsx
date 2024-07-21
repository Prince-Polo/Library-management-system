import React, { useState, useEffect, useCallback } from 'react'
import { useHistory } from 'react-router';
import { useStore, Info } from 'Plugins/GlobalVariables/store';
import { useStoreSelected, Selected } from 'Plugins/GlobalVariables/LibrarySetting';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/SharedComponents/ErrorMessage';
import { StudentSeatReservationMessage } from 'Plugins/StudentAPI/StudentSeatReservationMessage';

interface SeatReservationModalProps {
    isOpen: boolean;
    library: string;
    floor: string;
    area: string;
    seat: string;
    occupied: boolean;
    reported: string;
    confirmed: boolean;
    onClose: () => void;
}

export const SeatReservationModal: React.FC<SeatReservationModalProps> = ({ isOpen, library, floor, area, seat, occupied,reported,confirmed, onClose }) => {
    const info = useStore((state) => state.info);
    const setInfo = useStore((state) => state.setInfo);
    const setSelected = useStoreSelected((state) => state.setLibrary);
    const history = useHistory();
    const [success, setSuccess] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [message,setMessage]=useState<string>('');
    const [showReportedButton,setShowReportedButton]=useState<boolean>(false);
    const [showConfirmButton,setShowConfirmButton]=useState<boolean>(false);

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

    useEffect(() => {
        if (isOpen) {
            if (info.volunteerStatus === true) {
                setMessage('Please complete your volunteer work before reserving a seat.');
                setShowReportedButton(false);
                setShowConfirmButton(false);
            } else if (confirmed) {
                setMessage(`This seat is currently unavailable due to the following issues: ${reported}`);
                setShowReportedButton(false);
                setShowConfirmButton(false);
            } else if (occupied) {
                setMessage('This seat is already occupied.');
                setShowReportedButton(true);
                setShowConfirmButton(false);
            } else if (!success && info.sectionNumber !== "0" && info.seatNumber !== "0") {
                setMessage('You have already reserved a seat. Please do not reserve another one.');
                setShowReportedButton(true);
                setShowConfirmButton(false);
            } else if (!success) {
                setMessage(`You are reserving seat ${floor === '0' ? 'G' : `F${floor}`}${area}${seat.padStart(3, '0')} in ${area} area on floor ${floor} of ${library}.`);
                setShowReportedButton(true);
                setShowConfirmButton(true);
            }
        }
    }, [isOpen, info, confirmed, reported, occupied, success, floor, area, seat, library]);

    if(!isOpen) return null;
    return (
        <>
            <div style={overlayStyle} onClick={onClose} />
            <div style={modalStyle}>
                <h2>Seat Reservation</h2>
                {!success ? (
                    <>
                        <p>{message}</p>
                        <p style={{color:'red', fontSize:'16px', marginTop:'-10px'}}>{reported && !confirmed && !occupied && info.seatNumber==='0'?`The seat probably has issues, do you still want to select it?`:``}</p>
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
                        {showReportedButton && (
                        <button
                            style={reportButtonStyle}
                            onClick={handleReportIssue}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'darkorange')}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#FFA500')}
                        >
                            Report Issue
                        </button>
                            )}
                    </>
                ) : (
                    <>
                        <p>{info.userName}, you have successfully reserved seat {floor==='0'?'G':`F${floor}`}{area}{seat.padStart(3,'0')} in {area} area on floor {floor} of {library} library.</p>
                        <button
                            style={successButtonStyle}
                            onClick={handleCancel}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'lightgreen')}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#32CD32')}
                        >
                            OK
                        </button>
                    </>
                )}
            </div>
            <ErrorModal message={error} onClose={onClose} />
            {/*{showReportedModal&&(*/}
            {/*    <div style={reportModalStyle}>*/}
            {/*        <h1 style={{ color: 'firebrick', fontSize: '30px', background: 'rgba(240,128,128,0.6)' }}>*/}
            {/*            Warning*/}
            {/*        </h1>*/}
            {/*        <p>This seat probably has the following issues:</p>*/}
            {/*        <p>${seatDamage}${seatDamage === '' ? '' : ','}${deskDamage}${deskDamage === '' ? '' : ','}${electricalIssue}${electricalIssue === '' ? '' : ','}${other}.</p>*/}
            {/*        <p>Do you still want to select it?</p>*/}
            {/*        <button*/}
            {/*            style={cancelButtonStyle}*/}
            {/*            onClick={handleCancel}*/}
            {/*            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'lightgrey')}*/}
            {/*            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = 'white')}*/}
            {/*        >*/}
            {/*            No*/}
            {/*        </button>*/}
            {/*        <button*/}
            {/*            style={confirmButtonStyle}*/}
            {/*            onClick={()=>setShowReportedModal(false)}*/}
            {/*            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'lightgreen')}*/}
            {/*            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#20B2AA')}*/}
            {/*        >*/}
            {/*            Yes*/}
            {/*        </button>*/}
            {/*</div>)}*/}
        </>
    );
};

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

const reportModalStyle: React.CSSProperties = {
    position: 'fixed',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    backgroundColor: 'white',
    padding: '20px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    zIndex: 1001,
    textAlign: 'center',
}

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