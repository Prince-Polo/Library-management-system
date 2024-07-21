import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useStore, Info } from 'Plugins/GlobalVariables/store';
import { StudentSeatLeaveMessage } from 'Plugins/StudentAPI/StudentSeatLeaveMessage';
import { ErrorModal, sendPostRequest, SuccessModal } from 'Pages/SharedComponents/ErrorMessage';

interface ReservationPaneProps {
    info: Info;
    setInfo: (info: Info) => void;
}

export const ReservationPane: React.FC<ReservationPaneProps> = ({ info, setInfo }) => {
    const [showFullLeaveModal, setShowFullLeaveModal] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleFullLeave = () => {
        setShowFullLeaveModal(true);
    };

    const handleConfirmFullLeave = () => {
        const studentLeaveMessage = new StudentSeatLeaveMessage(info.token, info.floor, info.sectionNumber, info.seatNumber);
        sendPostRequest(studentLeaveMessage, setError, (response) => {
            console.log('Student leave message sent:', response);
            setInfo({
                ...info,
                floor: "0",
                sectionNumber: "0",
                seatNumber: "0"
            });
        });
        setShowFullLeaveModal(false);
    };

    const handleClose = () => {
        setError(null);
    };

    const handleCloseSuccess = () => {
        setSuccess(null);
    };

    return (
        <div style={paneStyle}>
            <h3>Reservation Record</h3>
            {info.floor === "0" && info.sectionNumber === "0" && info.seatNumber === "0" ? (
                <p>No Reservation</p>
            ) : (
                <>
                    <p>Floor: {info.floor}</p>
                    <p>Section: {info.sectionNumber}</p>
                    <p>Seat: {info.floor === '0' ? 'G' : `F${info.floor}`}{info.sectionNumber}{info.seatNumber.padStart(3, '0')}</p>
                </>
            )}
            {info.sectionNumber !== "0" && (
                <div style={buttonsContainerStyle}>
                    <button style={leaveButtonStyle} onClick={handleFullLeave}
                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'lightgreen'}
                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#32CD32'}>
                        Full Leave
                    </button>
                </div>
            )}
            <>
                {showFullLeaveModal && (
                    <div style={overlayStyle} onClick={() => setShowFullLeaveModal(false)}>
                        <div style={modalStyle}>
                            <h2>{info.userName}, you have successfully left seat {info.floor === '0' ? 'G' : `F${info.floor}`}{info.sectionNumber}{info.seatNumber.padStart(3, '0')}.</h2>
                            <button style={buttonStyle} onClick={handleConfirmFullLeave}>OK</button>
                        </div>
                    </div>
                )}
            </>
            <ErrorModal message={error} onClose={handleClose} />
            <SuccessModal message={success} onClose={handleCloseSuccess} />
        </div>
    );
};

const paneStyle: React.CSSProperties = {
    backgroundColor: '#f9f9f9',
    border: '1px solid #ddd',
    borderRadius: '8px',
    padding: '20px',
    marginBottom: '20px',
    textAlign: 'left',
};

const buttonsContainerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    gap: '10px',
    marginBottom: '20px',
};

const leaveButtonStyle: React.CSSProperties = {
    padding: '10px 20px',
    backgroundColor: '#32CD32',
    color: 'white',
    border: '1px solid black',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
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
    backgroundColor: 'white',
    transition: 'background-color 0.3s',
};

