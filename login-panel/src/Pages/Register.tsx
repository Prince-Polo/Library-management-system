import React, { ChangeEvent, FormEvent, useState } from 'react';
import { useHistory } from 'react-router';
import { AdminLoginMessage } from 'Plugins/AdminAPI/AdminLoginMessage';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import './app.css';

interface FormProps {
    title: string;
    fields: { name: string, type: string, label: string }[];
    createMessage: (formData: any) => AdminLoginMessage;
    onSuccess?: () => void;
}

const GenericForm: React.FC<FormProps> = ({ title, fields, createMessage, onSuccess }) => {
    const [formData, setFormData] = useState<any>({});
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const history = useHistory();

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        const message = createMessage(formData);
        console.log(message);
        sendPostRequest(message, setError, () => {
            setSuccess("Operation(s) successful");
            if (onSuccess) onSuccess();
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

const AdminLogins: React.FC = () => {
    const history = useHistory();
    const fields = [
        { name: 'name', type: 'text', label: 'Name', required: true },
        { name: 'password', type: 'password', label: 'Password', required: true }
    ];

    const handleSuccess = () => {
        setTimeout(() => history.push('/addJob'), 2000); // 只跳转到 AddJobPage
    };

    const createMessage = (formData: any) => new AdminLoginMessage(formData.name, formData.password);

    return <GenericForm title="Administrator Login" fields={fields} createMessage={createMessage} onSuccess={handleSuccess} />;
};

export default AdminLogins;
// 其他不变的组件代码保持原样
const StudentRegisters: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name', required: true },
        { name: 'password', type: 'password', label: 'Password', required: true },
        { name: 'number', type: 'textarea', label: 'Number', required: true }
    ];

    const createMessage = (formData: any) => new StudentRegisterMessage(formData.name, formData.password, formData.number);

    return <GenericForm title="Student Register" fields={fields} createMessage={createMessage} />;
};

const StudentLogins: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name', required: true },
        { name: 'password', type: 'password', label: 'Password', required: true },
        { name: 'number', type: 'textarea', label: 'Number', required: false }
    ];

    const createMessage = (formData: any) => new StudentLoginMessage(formData.name, formData.password, "");

    return <GenericForm title="Student Login" fields={fields} createMessage={createMessage} />;
};

const AdminRegisters: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name', required: true },
        { name: 'password', type: 'password', label: 'Password', required: true },
        { name: 'id', type: 'textarea', label: 'ID', required: true }
    ];

    const createMessage = (formData: any) => new AdminRegisterMessage(formData.name, formData.password, formData.id);

    return <GenericForm title="Administrator Register" fields={fields} createMessage={createMessage} />;
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
