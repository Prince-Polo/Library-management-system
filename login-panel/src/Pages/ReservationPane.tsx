import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useStore, Info } from './store';
import { StudentSeatLeaveMessage } from 'Plugins/StudentAPI/StudentSeatLeaveMessage';
import { UpdateViolationCountMessage } from 'Plugins/StudentAPI/UpdateViolationCountMessage';
import { ErrorModal, sendPostRequest, SuccessModal } from 'Pages/ErrorMessage'

interface ReservationPaneProps {
    info: Info;
    setInfo: (info: Info) => void;
}

export const ReservationPane: React.FC<ReservationPaneProps> = ({ info, setInfo }) => {
    const history = useHistory();
    const [showTempLeaveModal, setShowTempLeaveModal] = useState(false);
    const [showFullLeaveModal, setShowFullLeaveModal] = useState(false);
    const [tempLeaveTime, setTempLeaveTime] = useState<number | null>(null);
    const [error,setError] = useState<string | null>(null);
    const [success,setSuccess] = useState<string | null>(null);

    useEffect(() => {
        let interval: NodeJS.Timeout;
        if (tempLeaveTime !== null) {
            interval = setInterval(() => {
                setTempLeaveTime((prevTime) => {
                    if (prevTime && prevTime > 0) {
                        return prevTime - 1;
                    } else {
                        clearInterval(interval);
                        handleSeatRelease();
                        return null;
                    }
                });
            }, 1000);
        }
        return () => clearInterval(interval);
    }, [tempLeaveTime]);

    const handleTempLeave = () => {
        setShowTempLeaveModal(true);
        setTempLeaveTime(60); // 1 minute in seconds
    };

    const handleFullLeave = () => {
        setShowFullLeaveModal(true);
    };

    const handleSeatRelease = () => {
        // Send messages to release seat and update violation count
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
        setShowTempLeaveModal(false);
    };

    const handleConfirmFullLeave = () => {
        // Send messages to release seat
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

    const handleSignIn = () => {
        setTempLeaveTime(null);
        console.log('Signed in');
    };

    const formatTime = (seconds: number) => {
        const minutes = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${minutes} minutes ${secs} seconds`;
    };

    const handleClose=()=>{
        setError(null);
    }

    const handleCloseSucess=()=>{
        setSuccess(null);
    }

    return (
        <div style={paneStyle}>
            <h3>Reservation Record</h3>
            <p>Floor: {info.floor}</p>
            <p>Section: {info.sectionNumber}</p>
            <p>Seat: {info.floor==='0'?'G':`F${info.floor}`}{info.sectionNumber}{info.seatNumber.padStart(3,'0')}</p>
            <div style={buttonsContainerStyle}>
                <button style={leaveButtonStyle} onClick={handleTempLeave}
                        onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'lightgreen'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#32CD32'}>
                    Temporary Leave
                </button>
                <button style={leaveButtonStyle} onClick={handleFullLeave}
                        onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'lightgreen'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#32CD32'}>
                    Full Leave
                </button>
            </div>
            {tempLeaveTime !== null && (
                <div style={signOutContainerStyle}>
                    <p>Temporary leave remaining: {formatTime(tempLeaveTime)}</p>
                    <button style={signOutButtonStyle} onClick={handleSignIn}
                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'lightgreen'}
                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#32CD32'}>
                        Sign In
                    </button>
                </div>
            )}
            <>
                {showTempLeaveModal && (
                    <div style={overlayStyle} onClick={() => setShowTempLeaveModal(false)}>
                        <div style={modalStyle}>
                            <h2>Temporary Leave Successful</h2>
                            <button style={buttonStyle} onClick={() => setShowTempLeaveModal(false)}>OK</button>
                        </div>
                    </div>
                )}
                {showFullLeaveModal && (
                    <div style={overlayStyle} onClick={() => setShowFullLeaveModal(false)}>
                        <div style={modalStyle}>
                            <h2>{info.userName}, you have successfully left seat {info.floor==='0'?'G':`F${info.floor}`}{info.sectionNumber}{info.seatNumber.padStart(3,'0')}</h2>
                            <button style={buttonStyle} onClick={handleConfirmFullLeave}>OK</button>
                        </div>
                    </div>
                )}
            </>
            <ErrorModal message={error} onClose={handleClose}/>
            <SuccessModal message={success} onClose={handleCloseSucess}/>
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

const signOutContainerStyle: React.CSSProperties = {
    textAlign: 'left',
    marginTop: '10px',
};

const signOutButtonStyle: React.CSSProperties = {
    ...leaveButtonStyle,
    backgroundColor: '#32CD32',
    marginTop: '10px',
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
