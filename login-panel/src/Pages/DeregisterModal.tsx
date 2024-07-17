import React, { useState,useEffect } from 'react'
import {useHistory} from 'react-router'
import {Info, useStore, useKeys } from './store'
import {StudentDeleteMessage} from 'Plugins/StudentAPI/StudentDeleteMessage'
import {StudentSeatLeaveMessage} from 'Plugins/StudentAPI/StudentSeatLeaveMessage'
import {sendPostRequest,ErrorModal,SuccessModal} from 'Pages/ErrorMessage'

interface DeleteAccountModalProps {
    isOpen: boolean;
    info: Info | null;
    onClose: () => void;
    onConfirm: () => void;
    onError: ()=>void;
    deleteForm: { userName: string; password: string; number: string; confirmation: string };
    setDeleteForm: React.Dispatch<React.SetStateAction<{ userName: string; password: string; number: string; confirmation: string }>>;
}

export const DeleteAccountModal: React.FC<DeleteAccountModalProps> = ({ isOpen, info,onClose,onConfirm,onError, deleteForm, setDeleteForm }) => {
    const history=useHistory();
    const keys = useKeys((state) => state.Keys);
    const setKeys = useKeys((state) => state.setKeys);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const handleDeleteChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setDeleteForm({ ...deleteForm, [e.target.name]: e.target.value });
    };
    const [isMounted, setIsMounted] = useState(true);

    useEffect(() => {
        setIsMounted(true);
        return () => {
            setIsMounted(false);
        };
    }, []);

    const handleDeleteConfirm = () => {
        const message=new StudentDeleteMessage(deleteForm.number,deleteForm.password);
        const seatMessage=new StudentSeatLeaveMessage(info.token, info.floor, info.sectionNumber, info.seatNumber);
        const handleSuccess=(successMsg:any)=>{
            setSuccess(successMsg);
        }
        console.log(message);
        console.log("Deletion input:",deleteForm);
        if (
            info&&
            deleteForm.userName === info.userName &&
            deleteForm.confirmation === 'I confirm deregistration.'
        ) {
            if(info.seatNumber!="0"&&info.sectionNumber!="0")
                sendPostRequest(seatMessage,setError,(response)=>{
                    handleSuccess("The seat you have selected has already been cleared.");
                })
            sendPostRequest(message,setError,(responseMsg)=>{
                const parsedResponse=JSON.stringify(responseMsg);
                if(parsedResponse.includes("error"))
                    onError();
                else {
                    setTimeout(()=>handleSuccess(responseMsg),2000);
                    onConfirm();
                }
            });
        } else {
            setError('Wrong input! Please try again.');
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
                <h2>Deregister</h2>
                <h3 style={{color:'red'}}>If you confirm, the seat you have selected will be cleared.</h3>
                <div style={{textAlign: 'left'}}>
                <label>User name</label>
                    <input
                        type="text"
                        name="userName"
                        placeholder="username"
                        value={deleteForm.userName}
                        onChange={handleDeleteChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                <label>Password</label>
                    <input
                        type="password"
                        name="password"
                        placeholder="password"
                        value={deleteForm.password}
                        onChange={handleDeleteChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                <label>Number</label>
                    <input
                        type="text"
                        name="number"
                        placeholder="number"
                        value={deleteForm.number}
                        onChange={handleDeleteChange}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                <label>Confirmation:"I confirm deregistration."</label>
                    <input
                        type="text"
                        name="confirmation"
                        placeholder="I confirm deregistration."
                        value={deleteForm.confirmation}
                        onChange={handleDeleteChange}
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
                            Cancel
                        </button>
                        <button
                            onClick={handleDeleteConfirm}
                            style={{ ...buttonStyle, transition: 'background-color 0.3s' }}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = 'orange')}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '#007bff')}
                        >
                            Confirm deregistration.
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

