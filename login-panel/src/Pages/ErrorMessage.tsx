import React,{useEffect}  from 'react';
import './Styles/app.css'
import { API } from 'Plugins/CommonUtils/API'
import axios, { isAxiosError } from 'axios'

export const sendPostRequest = async (
    message:API,
    setError: (error: string | null) => void,
    onSuccess?: (successMsg:any) => void
) => {
    try {
        const response = await axios.post(message.getURL(), JSON.stringify(message), {
            headers: { 'Content-Type': 'application/json' },
        });
        console.log('Response status:', response.status);
        console.log('Response body:', response.data);
        if (response.status === 200 && onSuccess) {
            onSuccess(response.data);
        }
    } catch (error) {
        if (isAxiosError(error)) {
            if (error.response && error.response.data) {
                console.error('Error sending request:', error.response.data);
                setError(`Error sending request: ${error.response.data.error}`);
            } else {
                console.error('Error sending request:', error.message);
                setError(`Error sending request: ${error.message}`);
            }
        } else {
            console.error('Unexpected error:', error);
            setError('An unexpected error occurred.');
        }
    }
};

export const ErrorModal:React.FC<{ message: string | null; onClose: () => void; }> = ({ message, onClose }) => {
    if (!message) return null;
    return (
        <div className={'overlay'}>
            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
                <p>{message}</p>
            </div>
        </div>
    );
};
export const SuccessModal: React.FC<{ message: string | null; onClose: () => void }> = ({ message, onClose }) => {
    useEffect(() => {
        if (message) {
            const timer = setTimeout(() => {
                onClose();
            }, 1900); // 1.9 seconds

            return () => clearTimeout(timer); // Cleanup the timer on unmount
        }
    }, [message, onClose]);
    if (!message) return null;
    return (
            <div className={'overlay'}>
                <div className="modal-content">
                    <span className="close" onClick={onClose}>&times;</span>
                    <p>{message}</p>
                </div>
        </div>
    );
};



