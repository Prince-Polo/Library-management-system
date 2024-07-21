import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useStore } from './store';
import { DeleteAccountModal } from 'Pages/DeregisterModal';
import { UpdateInfoModal } from 'Pages/UpdateInfoModal';
import { ReservationPane } from 'Pages/ReservationPane';
import myImage from '../Images/FrontPage.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faUserEdit, faUserSlash } from '@fortawesome/free-solid-svg-icons';

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
        <div style={backgroundStyle}>
            <div style={overlayStyle}>
                <div style={boxStyle}>
                    <div style={headerStyle}>
                        <button onClick={() => history.push("/student")} style={backButtonStyle}
                                onMouseEnter={() => setHover(true)}
                                onMouseLeave={() => setHover(false)}>
                            &lt;
                        </button>
                        <h1>My Center</h1>
                        <button onClick={handleLogout} style={logoutButtonStyle}
                                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'orange'}
                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>
                            Logout
                        </button>
                    </div>
                    <div style={infoStyle}>
                        <h2>Hello, {info.userName}!</h2>
                    </div>
                    <div style={paneStyle}>
                        <h3 style={volunteerHeaderStyle}>Volunteer Information</h3>
                        <div style={volunteerInfoStyle}>
                            <p style={volunteerDetailStyle}><strong>Volunteer Hours:</strong> {info.volunteerHours} hours</p>
                            <p style={volunteerDetailStyle}><strong>Currently a volunteer?:</strong> {info.volunteerStatus === "true" ? 'Yes' : 'No'}</p>
                        </div>
                    </div>
                    <ReservationPane info={info} setInfo={setInfo} />
                    <div style={buttonsContainerStyle}>
                        <button style={buttonStyle} onClick={handleApplyVolunteer} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'orange'}
                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>
                            <FontAwesomeIcon icon={faUser} style={iconStyle} />
                            <span>Volunteer</span>
                        </button>
                        <button style={buttonStyle} onClick={handleUpdateAccount} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'orange'}
                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>
                            <FontAwesomeIcon icon={faUserEdit} style={iconStyle} />
                            <span>Edit Info</span>
                        </button>
                        <button style={buttonStyle} onClick={handleDeleteAccount} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'orange'}
                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}>
                            <FontAwesomeIcon icon={faUserSlash} style={iconStyle} />
                            <span>Deregister</span>
                        </button>
                    </div>
                    <DeleteAccountModal
                        isOpen={showDeleteModal}
                        info={info}
                        onConfirm={() => {
                            setDeleteForm({ userName: '', password: '', number: '', confirmation: '' });
                            setTimeout(() => history.push("/"), 2000);
                        }}
                        onClose={handleCloseModal}
                        onError={() => setTimeout(() => handleCloseModal(), 2000)}
                        deleteForm={deleteForm}
                        setDeleteForm={setDeleteForm}
                    />
                    <UpdateInfoModal isOpen={showUpdateModal}
                                     info={info}
                                     onClose={handleCloseModal}
                                     onConfirm={() => {
                                         setInfoForm({ userName: '', password: '', number: '', newPassword: '', confirmation: '' });
                                         setTimeout(() => { setShowUpdateModal(false); }, 2000);
                                     }}
                                     onError={() => setTimeout(() => handleCloseModal(), 2000)}
                                     InfoForm={infoForm}
                                     setInfoForm={setInfoForm}
                    />
                </div>
            </div>
        </div>
    );
};

const backgroundStyle: React.CSSProperties = {
    backgroundImage: `url(${myImage})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    height: '100vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
};

const boxStyle: React.CSSProperties = {
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    padding: '30px',
    borderRadius: '20px',
    display: 'flex',
    flexDirection: 'column' as const,
    alignItems: 'center',
    width: '80%',
};

const headerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px 20px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderRadius: '8px',
    margin: '-5px 0px',
    width: '100%',
};

const infoStyle: React.CSSProperties = {
    margin: '0px 0px',
    textAlign: 'center',
    color: 'black',
    fontSize: '24px', // Make font larger
    fontWeight: 'bold', // Make font bold
};

const paneStyle: React.CSSProperties = {
    backgroundColor: '#f9f9f9',
    border: '1px solid #ddd',
    borderRadius: '8px',
    padding: '20px',
    margin: '0px 20px',
    textAlign: 'left',
    width: '95%',
};

const volunteerHeaderStyle: React.CSSProperties = {
    textAlign: 'center',
    fontSize: '28px', // Increase the font size
    margin: '5px 5px',
    fontWeight: 'bold', // Make font bold
};

const volunteerInfoStyle: React.CSSProperties = {
    textAlign: 'left',
    padding: '0 20px',
    margin: '20px  0px',
};

const volunteerDetailStyle: React.CSSProperties = {
    fontSize: '24px', // Increase the font size
    margin: '5px 0px',
    fontWeight: 'bold', // Make font bold
};

const buttonsContainerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    gap: '10px',
    marginBottom: '0px',
    width: '100%',
};

const buttonStyle: React.CSSProperties = {
    padding: '10px 20px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
    width: '30%',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    fontSize: '18px', // Make font larger
    fontWeight: 'bold', // Make font bold
};

const logoutButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    width: 'auto',
};

const backButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    backgroundColor: 'transparent',
    color: 'white',
    fontSize: '24px', // Increase the font size
    transition: 'background-color 0.3s',
    width: 'auto',
};

const iconStyle: React.CSSProperties = {
    marginBottom: '5px',
    fontSize: '24px', // Increase the icon size
};

export default MyCenter;
