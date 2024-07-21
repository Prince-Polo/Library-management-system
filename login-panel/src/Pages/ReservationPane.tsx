import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useStore, Info } from './store';
import { StudentSeatLeaveMessage } from 'Plugins/StudentAPI/StudentSeatLeaveMessage';
import { UpdateViolationCountMessage } from 'Plugins/StudentAPI/UpdateViolationCountMessage';
import { ErrorModal, sendPostRequest, SuccessModal } from 'Pages/ErrorMessage';

interface ReservationPaneProps {
    info: Info;
    setInfo: (info: Info) => void;
}

export const ReservationPane: React.FC<ReservationPaneProps> = ({ info, setInfo }) => {
    const history = useHistory();
    const [showFullLeaveModal, setShowFullLeaveModal] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleFullLeave = () => {
        setShowFullLeaveModal(true);
    };

    const handleSeatRelease = () => {
        const studentLeaveMessage = new StudentSeatLeaveMessage(info.token, info.floor, info.sectionNumber, info.seatNumber);
        const updateViolationCountMessage = new UpdateViolationCountMessage(info.token, info.violationCount.toString());
        sendPostRequest(studentLeaveMessage, setError, (response) => {
            console.log('Student leave message sent:', response);
        });
        sendPostRequest(updateViolationCountMessage, setError, (response) => {
            console.log('Update violation count message sent:', response);
            setInfo({
                ...info,
                violationCount: info.violationCount + 1,
                floor: "0",
                sectionNumber: "0",
                seatNumber: "0"
            });
        });
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
            <h3 style={reservationHeaderStyle}>Reservation Record</h3>
            {info.floor === '0' && info.sectionNumber === '0' && info.seatNumber === '0' ? (
                <p style={reservationHeaderStyle}>已离开</p>
            ) : (
                <>
                        <p style={reservationDetailStyle}><strong>Floor:</strong> {info.floor}</p>
                        <p style={reservationDetailStyle}><strong>Section:</strong> {info.sectionNumber}</p>
                        <p style={reservationDetailStyle}><strong>Seat:</strong> {info.floor === '0' ? 'G' : `F${info.floor}`}{info.sectionNumber}{info.seatNumber.padStart(3, '0')}</p>
       </>
            )}
            {info.floor !== '0' && (
                <div style={buttonsContainerStyle}>
                    <button style={leaveButtonStyle} onClick={handleFullLeave}
                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'lightgreen'}
                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#32CD32'}>
                        Leave
                    </button>
                </div>
            )}
            <>
                {showFullLeaveModal && (
                    <div style={overlayStyle} onClick={() => setShowFullLeaveModal(false)}>
                        <div style={modalStyle}>
                            <h2>{info.userName}, you have successfully left
                                seat {info.floor === '0' ? 'G' : `F${info.floor}`}{info.sectionNumber}{info.seatNumber.padStart(3, '0')}</h2>
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
    margin: '20px 0px',
    textAlign: 'left',
    width: '95%',
};

const reservationHeaderStyle: React.CSSProperties = {
    textAlign: 'center',
    fontSize: '28px', // Increase the font size
    margin: '0 0px',
    fontWeight: 'bold', // Make font bold
};

const reservationInfoStyle: React.CSSProperties = {
    textAlign: 'left',
    padding: '0 20px',
};

const reservationDetailStyle: React.CSSProperties = {
    fontSize: '24px', // Increase the font size
    margin: '10px 0px',
    fontWeight: 'bold', // Make font bold
};



const buttonsContainerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    marginTop: '20px',
};

const leaveButtonStyle: React.CSSProperties = {
    padding: '10px 20px',
    backgroundColor: '#32CD32',
    color: 'white',
    border: '1px solid black',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
    fontSize: '18px', // 设置字体大小
    fontWeight: 'bold', // 设置字体粗细
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

export default ReservationPane;
