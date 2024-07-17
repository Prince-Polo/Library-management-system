import React, { useState } from 'react'
import {Info, useStore, useKeys } from './store'
import {StudentUpdateMessage} from 'Plugins/StudentAPI/StudentUpdateMessage'
import {sendPostRequest,ErrorModal,SuccessModal} from 'Pages/ErrorMessage'

interface UpdateInfoModalProps {
    isOpen: boolean;
    info: Info | null;
    onClose: () => void;
    onConfirm: () => void;
    onError: ()=>void;
    InfoForm: { userName: string; password: string; number: string; newPassword: string, confirmation: string };
    setInfoForm: React.Dispatch<React.SetStateAction<{ userName: string; password: string; number: string; newPassword:string,confirmation: string }>>;
}

export const UpdateInfoModal: React.FC<UpdateInfoModalProps> = ({ isOpen, info,onClose, onConfirm, onError,InfoForm, setInfoForm }) => {
    const keys=useKeys((state) => state.Keys);
    const setKeys = useKeys((state) => state.setKeys);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const handleInfoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setInfoForm({ ...InfoForm, [e.target.name]: e.target.value });
    };
    const handleUpdateConfirm = () => {
        const message=new StudentUpdateMessage(InfoForm.userName,InfoForm.password,InfoForm.number,InfoForm.newPassword);
        console.log(message)
        console.log(info)
        console.log(InfoForm)
        if (
            info&&
            InfoForm.userName === info.userName
        ){
        if(InfoForm.confirmation === InfoForm.newPassword){
            console.log("Password change:",InfoForm.newPassword);
            sendPostRequest(message,setError,(responseMsg)=>{
                    setSuccess(responseMsg);
                    onConfirm();
            })
        }
        else {
            setError("Failed to confirm new password.");
        }
        }else {
            setError('Wrong user information! Please try again.');
        }
    };
    const closeModal = () => {
        setError(null);
    };

    const closeSuccessModal = () => {
        setSuccess(null);
    };
    if (!isOpen) return null;

    return (
        <>
            <div style={overlayStyle} onClick={onClose} />
            <div style={modalStyle}>
                <h2>Update Password</h2>
                <div style={{ textAlign: 'left', fontFamily: 'Times New Roman', color: 'orangered' }}>
                    <label>User name</label>
                    <input
                        type="text"
                        name="userName"
                        placeholder="username"
                        value={InfoForm.userName}
                        onChange={handleInfoChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                    <label>Number</label>
                    <input
                        type="text"
                        name="number"
                        placeholder="number"
                        value={InfoForm.number}
                        onChange={handleInfoChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                    <label>Password</label>
                    <input
                        type="password"
                        name="password"
                        placeholder="password"
                        value={InfoForm.password}
                        onChange={handleInfoChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                    <label>New password.</label>
                    <input
                        type="password"
                        name="newPassword"
                        placeholder="New password."
                        value={InfoForm.newPassword}
                        onChange={handleInfoChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                    <label>Confirm new password.</label>
                    <input
                        type="password"
                        name="confirmation"
                        placeholder="Confirm new password."
                        value={InfoForm.confirmation}
                        onChange={handleInfoChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                </div>
                <div style={buttonsContainerStyle}>
                    <button
                        onClick={onClose}
                        style={{ ...buttonStyle, transition: 'background-color 0.3s' }}
                        onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = '#0187ff')}
                        onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#007bff')}
                    >
                        Cancel.
                    </button>
                    <button
                        onClick={handleUpdateConfirm}
                        style={{ ...buttonStyle, transition: 'background-color 0.3s' }}
                        onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'orange')}
                        onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#007bff')}
                    >
                        Confirm update.
                    </button>
                </div>
            </div>
            <ErrorModal message={error} onClose={closeModal}/>
            <SuccessModal message={success} onClose={closeSuccessModal} />
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
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
};

const buttonsContainerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    gap: '10px',
};

