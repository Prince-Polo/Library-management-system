import React, { ChangeEvent, FormEvent, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { API } from 'Plugins/CommonUtils/API';
import { useHistory } from 'react-router';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import { useStore, Info, useKeys } from "./store";
import myImage from '../Images/LoginImage.png'; // 请根据你的路径调整
import { faUser, faLock, faIdCard, faUserShield } from '@fortawesome/free-solid-svg-icons';

interface FormProps {
    title: string;
    fields: { name: string, type: string, label: string, icon: IconDefinition }[];
    createMessage: (formData: any) => API;
    onSuccess?: (responseMsg: any) => void;
}

export const RegisterLoginForm: React.FC<FormProps> = ({ title, fields, createMessage, onSuccess }) => {
    const [formData, setFormData] = useState<any>({});
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [isRegister, setIsRegister] = useState<boolean>(false);
    const history = useHistory();
    const setInfo = useStore((state) => state.setInfo);
    const setKeys = useKeys((state) => state.setKeys);
    const setVolunteerModal = useStore((state) => state.setVolunteerModal);
    const maxPasswordLength = 12;
    const minPasswordLength = 8;

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        const message = createMessage(formData);
        if(message.type==="StudentRegisterMessage") {
            const password = formData.password || '';
            if (password.length < minPasswordLength) {
                setError(`Password must be at least ${minPasswordLength} characters long.`);
                return;
            } else if (password.length > maxPasswordLength) {
                setError(`Password must be at most ${maxPasswordLength} characters long.`);
                return;
            }
        }
        sendPostRequest(message, setError, (responseMsg) => {
            if (message.type === "StudentLoginMessage" || message.type === "StudentRegisterMessage")
                try {
                    const parsedResponse = JSON.parse(responseMsg);
                    parsedResponse.userName = formData.name;
                    if (parsedResponse.number === "")
                        setError("Wrong number.");
                    else if ("valid" in parsedResponse && parsedResponse["valid"] === true) {
                        setSuccess("Login successful! Redirecting to the main page...");
                        const info = parsedResponse as Info;
                        setInfo(info);
                        if(info.volunteerStatus!="false") {
                            setVolunteerModal(true);
                        }
                        setTimeout(() => history.push('/Student'), 2000);
                    } else
                        setSuccess("Register successful! ");
                    if (onSuccess) onSuccess(responseMsg);
                } catch (error) {
                    setError("Failed to parse response.");
                }
            else {
                setSuccess(responseMsg);
                if (message.type === "AdminLoginMessage") {
                    setTimeout(() => history.push('/addJob'), 2000); // 跳转到 AddJobPage
                }
            }
        });
    };

    const toggleRegister = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();
        setIsRegister(!isRegister);
        if(title==="Administrator Login")
            history.push('/AdminRegister');
        else if(title==="Administrator Register")
            history.push('/AdminLogin');
        else if(title==="Student Login")
            history.push('/StudentRegister');
        else if(title==="Student Register")
            history.push('/StudentLogin');
    };

    const closeModal = () => {
        setError(null);
    };

    const closeSuccessModal = () => {
        setSuccess(null);
    };

    const formFields = isRegister ? fields.filter(field => field.name !== 'number') : fields;

    return (
        <div style={containerStyle}>
            <div style={leftContainerStyle}>
                <img src={myImage} alt="Login Illustration" style={imageStyle} />
            </div>
            <div style={rightContainerStyle}>
                <div style={formContainerStyle}>
                    <h2>{title}</h2>
                    <form onSubmit={handleSubmit}>
                        {formFields.map(field => (
                            <div style={formGroupStyle} key={field.name}>
                                <label htmlFor={field.name}>{field.label}:</label>
                                <div style={inputContainerStyle}>
                                    <FontAwesomeIcon icon={field.icon} />
                                    <input
                                        type={field.type}
                                        id={field.name}
                                        name={field.name}
                                        value={formData[field.name] || ''}
                                        onChange={handleChange}
                                        required
                                        style={inputStyle}
                                    />
                                </div>
                            </div>
                        ))}
                        <div style={buttonGroupStyle}>
                            <button type="submit" style={buttonPrimaryStyle}>Submit</button>
                            <button
                                type="button"
                                style={buttonPrimaryStyle}
                                onClick={() => history.push("/")}
                            >
                                Return to first page
                            </button>
                        </div>
                    </form>
                    <div style={toggleContainerStyle}>
                        <button onClick={toggleRegister} style={linkStyle}>
                            {isRegister ? "Already have an Account? Login here." : "Don't have an Account? Register first."}
                        </button>
                    </div>
                </div>
            </div>
            <ErrorModal message={error} onClose={closeModal} />
            <SuccessModal message={success} onClose={closeSuccessModal} />
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    display: 'flex',
    width: '100%',
    height: '100vh',
    background: 'rgba(245, 245, 245, 0.9)',
    fontFamily: 'Georgia, serif',
};

const leftContainerStyle: React.CSSProperties = {
    width: '50%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    background: '#f5f5f5',
};

const rightContainerStyle: React.CSSProperties = {
    width: '50%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const formContainerStyle: React.CSSProperties = {
    background: 'rgba(255, 255, 255, 0.9)',
    padding: '40px',
    borderRadius: '10px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    textAlign: 'center',
    width: '80%',
};

const formGroupStyle: React.CSSProperties = {
    marginBottom: '15px',
    textAlign: 'left',
};

const inputContainerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
};

const inputStyle: React.CSSProperties = {
    flex: 1,
    padding: '10px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    marginLeft: '10px',
    fontSize: '14px',
};

const buttonGroupStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    marginTop: '20px',
};

const buttonPrimaryStyle: React.CSSProperties = {
    padding: '10px 20px',
    backgroundColor: '#8B4513',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'background-color 0.3s ease',
};

const buttonPrimaryHoverStyle: React.CSSProperties = {
    backgroundColor: '#5a3310',
};

const imageStyle: React.CSSProperties = {
    maxWidth: '100%',
    maxHeight: '100%',
};

const toggleContainerStyle: React.CSSProperties = {
    marginTop: '20px',
    textAlign: 'center',
};

const linkStyle: React.CSSProperties = {
    color: '#8B4513',
    textDecoration: 'none',
    cursor: 'pointer',
    background: 'none',
    border: 'none',
    padding: 0,
    font: 'inherit',
};
