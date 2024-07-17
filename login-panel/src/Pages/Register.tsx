import React, { ChangeEvent, FormEvent, useState } from 'react';
import { API } from 'Plugins/CommonUtils/API';
import { useHistory } from 'react-router';
import { StudentLoginMessage } from 'Plugins/StudentAPI/StudentLoginMessage';
import { StudentRegisterMessage } from 'Plugins/StudentAPI/StudentRegisterMessage';
import { AdminRegisterMessage } from 'Plugins/AdminAPI/AdminRegisterMessage';
import { AdminLoginMessage } from 'Plugins/AdminAPI/AdminLoginMessage';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import { useStore, Info, useKeys } from "./store";
import './Styles/app.css';

interface FormProps {
    title: string;
    fields: { name: string, type: string, label: string }[];
    createMessage: (formData: any) => API;
    onSuccess?: (responseMsg: any) => void;
}

const GenericForm: React.FC<FormProps> = ({ title, fields, createMessage, onSuccess }) => {
    const [formData, setFormData] = useState<any>({});
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const history = useHistory();
    const setInfo = useStore((state) => state.setInfo);
    const setKeys = useKeys((state) => state.setKeys);

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        const message = createMessage(formData);
        setKeys(formData.password);
        console.log(message);
        sendPostRequest(message, setError, (responseMsg) => {
            if (message.type === "StudentLoginMessage" || message.type === "StudentRegisterMessage") {
                try {
                    const parsedResponse = JSON.parse(responseMsg);
                    parsedResponse.userName = formData.name;
                    console.log("Result:", parsedResponse);
                    if (parsedResponse.number === "")
                        setError("Wrong number.");
                    else if ("valid" in parsedResponse && parsedResponse["valid"] === true) {
                        setSuccess("Login successful! Redirecting to the main page...");
                        const info = parsedResponse as Info;
                        setInfo(info);
                        console.log(info);
                        setTimeout(() => history.push('/Student'), 2000);
                    } else
                        setSuccess("Register successful!");
                    if (onSuccess) onSuccess(responseMsg);
                } catch (error) {
                    console.log("Parsing error:", error);
                    setError("Failed to parse response.");
                }
            } else {
                setSuccess(responseMsg);
                if (message.type === "AdminLoginMessage") {
                    if (onSuccess) onSuccess(responseMsg);
                    setTimeout(() => history.push('/addJob'), 2000); // 跳转到 AddJobPage
                }
            }
        });
    };

    const closeModal = () => {
        setError(null);
    };

    const closeSuccessModal = () => {
        setSuccess(null);
    };

    return (
        <div className="form-container">
            <div className="form">
                <h2>{title}</h2>
                <form onSubmit={handleSubmit}>
                    {fields.map(field => (
                        <div className="form-group" key={field.name}>
                            <label htmlFor={field.name}>{field.label}:</label>
                            <input
                                type={field.type}
                                id={field.name}
                                name={field.name}
                                value={formData[field.name] || ''}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    ))}
                    <div className="button-group" style={{ width: "100%" }}>
                        <button type="submit" className='button button-primary'>Submit</button>
                        <button
                            type="button"
                            className='button button-primary'
                            onClick={() => history.push("/")}
                        >
                            Return to first page
                        </button>
                    </div>
                </form>
            </div>
            <ErrorModal message={error} onClose={closeModal} />
            <SuccessModal message={success} onClose={closeSuccessModal} />
        </div>
    );
};

const StudentRegisters: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name' },
        { name: 'password', type: 'password', label: 'Password' },
        { name: 'number', type: 'textarea', label: 'Number' }
    ];

    const createMessage = (formData: any) => {
        return new StudentRegisterMessage(formData.name, formData.password, formData.number);
    };
    return <GenericForm title="Student Register" fields={fields} createMessage={createMessage} />;
};

const StudentLogins: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name' },
        { name: 'password', type: 'password', label: 'Password' },
        { name: 'number', type: 'textarea', label: 'Number' }
    ];
    const createMessage = (formData: any) => {
        return new StudentLoginMessage(formData.name, formData.password, formData.number);
    };
    return <GenericForm title="Student Login" fields={fields} createMessage={createMessage} />;
};

const AdminRegisters: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name' },
        { name: 'password', type: 'password', label: 'Password' },
        { name: 'id', type: 'textarea', label: 'ID' }
    ];

    const createMessage = (formData: any) => new AdminRegisterMessage(formData.name, formData.password, formData.id);
    return <GenericForm title="Administrator Register" fields={fields} createMessage={createMessage} />;
};

const AdminLogins: React.FC = () => {
    const history = useHistory();
    const [success, setSuccess] = useState<string | null>(null);

    const fields = [
        { name: 'name', type: 'text', label: 'Name' },
        { name: 'password', type: 'password', label: 'Password' }
    ];

    const handleSuccess = (responseMsg: any) => {
        setSuccess("Login successful! Redirecting to Add Job page...");
        setTimeout(() => history.push('/addJob'), 2000); // 跳转到 AddJobPage
    };

    const createMessage = (formData: any) => new AdminLoginMessage(formData.name, formData.password);

    const closeSuccessModal = () => {
        setSuccess(null);
    };

    return (
        <div>
            <GenericForm title="Administrator Login" fields={fields} createMessage={createMessage} onSuccess={handleSuccess} />
            <SuccessModal message={success} onClose={closeSuccessModal} />
        </div>
    );
};

export function StudentRegister() {
    return (
        <div style={{ background: 'aliceblue', opacity: '90%' }}>
            <StudentRegisters />
        </div>
    );
}

export function StudentLogin() {
    return (
        <div style={{ background: 'white', opacity: '90%' }}>
            <StudentLogins />
        </div>
    );
}

export function AdminRegister() {
    return (
        <div style={{ background: 'lightcyan', opacity: '90%', color: 'blueviolet' }}>
            <AdminRegisters />
        </div>
    );
}

export function AdminLogin() {
    return (
        <div style={{ background: 'lightyellow', opacity: '90%', color: 'deepskyblue' }}>
            <AdminLogins />
        </div>
    );
}
