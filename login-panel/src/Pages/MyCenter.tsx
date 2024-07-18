import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useStore } from './store';
import { DeleteAccountModal } from 'Pages/DeregisterModal';
import { UpdateInfoModal } from 'Pages/UpdateInfoModal';
import { ReservationPane } from 'Pages/ReservationPane';

export const MyCenter: React.FC = () => {
    const history = useHistory();
    const [showUpdateModal, setShowUpdateModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteForm, setDeleteForm] = useState({ userName: '', password: '', number: '', confirmation: '' });
    const [infoForm, setInfoForm] = useState({ userName: '', password: '', number: '', newPassword: '', confirmation: '' });
    const info = useStore((state) => state.info);
    const setInfo = useStore((state) => state.setInfo);
    const [hover, setHover] = useState(false);

    const handleLogout = () => {
        history.push("/");
        console.log('Logged out');
    };

    const handleUpdateAccount = () => {
        setShowUpdateModal(true);
    };

    const handleReportIssue = () => {
        setShowUpdateModal(false);
    }; // No implementation.

    const handleDeleteAccount = () => {
        setShowDeleteModal(true);
    };

    const handleCloseModal = () => {
        setDeleteForm({ userName: '', password: '', number: '', confirmation: '' });
        setInfoForm({ userName: '', password: '', number: '', newPassword: '', confirmation: '' });
        setShowUpdateModal(false);
        setShowDeleteModal(false);
    };

    const handleApplyVolunteer = () => {
        history.push('/volunteerapplication');
    };

    return (
        <div style={myCenterStyle}>
            {showUpdateModal && <div style={overlayStyle} onClick={handleCloseModal} />}
            <div style={headerStyle}>
                <button onClick={() => history.push("/student")} style={backButtonStyle}
                        onMouseEnter={() => setHover(true)}
                        onMouseLeave={() => setHover(false)}>
                    &lt;
                </button>
                <h1>My Center</h1>
                <button onClick={handleLogout} style={buttonStyle}
                        onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'orange'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>
                    Logout
                </button>
            </div>
            <div style={infoStyle}>
                <h2>Hello, {info.userName}!</h2>
                <p>Volunteer Hours: {info.volunteerHours} hours</p>
                <p>Currently a volunteer?: {info.volunteerStatus === "true" ? 'Yes' : 'No'}</p>
            </div>
            {info.volunteerStatus === "true" && (
                <div style={paneStyle}>
                    <h3>Volunteer Hours</h3>
                    <p>{info.volunteerHours} hours</p>
                </div>
            )}
            {(info.floor !== "0" && info.sectionNumber !== "0" && info.seatNumber !== "0") && (
                <ReservationPane info={info} setInfo={setInfo} />
            )}
            <div style={buttonsContainerStyle}>
                <button style={buttonStyle} onClick={handleApplyVolunteer} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#1199ff'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>Apply for a volunteer</button>
                <button style={buttonStyle} onClick={handleReportIssue} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#1199ff'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>Report Issues</button>
            </div>
            <div style={buttonsContainerStyle}>
                <button style={buttonStyle} onClick={handleUpdateAccount} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#1199ff'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>Change personal information</button>
                <button style={buttonStyle} onClick={handleDeleteAccount} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'orange'}
                        onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>Deregister</button>
            </div>
            <DeleteAccountModal
                isOpen={showDeleteModal}
                info={info}
                onConfirm={() => {
                    setDeleteForm({ userName: '', password: '', number: '', confirmation: '' });
                    setTimeout(() => history.push("/"), 2000);
                }}
                onClose={handleCloseModal}
                onError={()=>setTimeout(() => handleCloseModal(), 2000)}
                deleteForm={deleteForm}
                setDeleteForm={setDeleteForm}
            />
            <UpdateInfoModal isOpen={showUpdateModal}
                             info={info}
                             onClose={handleCloseModal}
                             onConfirm={() => {
                                 setInfoForm({ userName: '', password: '', number: '', newPassword: '', confirmation: '' })
                                 setTimeout(() => { setShowUpdateModal(false) }, 2000)
                             }}
                             onError={()=>setTimeout(()=>handleCloseModal(),2000)}
                             InfoForm={infoForm}
                             setInfoForm={setInfoForm}
            />
        </div>
    );
};

const myCenterStyle: React.CSSProperties = {
    textAlign: 'center',
    padding: '20px',
    fontFamily: 'Arial, sans-serif',
};

const headerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px 20px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderRadius: '8px',
    marginBottom: '20px',
};

const infoStyle: React.CSSProperties = {
    margin: '20px 0',
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

const buttonStyle: React.CSSProperties = {
    padding: '10px 20px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
};

const backButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    backgroundColor: 'transparent',
    color: 'white',
    fontSize: '24px', // Increase the font size
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