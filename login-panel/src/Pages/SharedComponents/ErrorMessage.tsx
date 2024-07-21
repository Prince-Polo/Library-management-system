import React, { useEffect } from 'react';
import axios, { isAxiosError } from 'axios';
import { API } from 'Plugins/CommonUtils/API';

export const sendPostRequest = async (
    message: API,
    setError: (error: string | null) => void,
    onSuccess?: (successMsg: any) => void
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

export const ErrorModal: React.FC<{ message: string | null; onClose: () => void; }> = ({ message, onClose }) => {
    if (!message) return null;
    return (
        <div style={overlayStyle}>
            <div style={modalContentStyle}>
                <span style={closeStyle} onClick={onClose}>&times;</span>
                <h2 style={errorTitleStyle}>Error</h2>
                <p style={errorTextStyle}>{message}</p>
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
        <div style={overlayStyle}>
            <div style={modalContentStyle}>
                <span style={closeStyle} onClick={onClose}>&times;</span>
                <p style={successTextStyle}>{message}</p>
            </div>
        </div>
    );
};

const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 2000,
};

const modalContentStyle: React.CSSProperties = {
    backgroundColor: 'white',
    padding: '20px',
    borderRadius: '18px',
    width: '61.8%',
    textAlign: 'center',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

const closeStyle: React.CSSProperties = {
    color: '#aaa',
    fontSize: '28px',
    fontWeight: 'bold',
    position: 'absolute',
    top: '5px',
    right: '12px',
    cursor: 'pointer',
};

const errorTextStyle: React.CSSProperties = {
    color: 'darkred',
};

const successTextStyle: React.CSSProperties = {
    color: 'black',
};

const errorTitleStyle: React.CSSProperties = {
    color: 'red',
    fontSize: '24px',
    marginBottom: '10px',
};
