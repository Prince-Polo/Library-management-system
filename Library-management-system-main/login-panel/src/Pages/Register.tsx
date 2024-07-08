import React, { ChangeEvent, FormEvent, useState } from 'react'
import { API } from 'Plugins/CommonUtils/API'
import { useHistory } from 'react-router'
import { PatientLoginMessage } from 'Plugins/PatientAPI/PatientLoginMessage'
import { PatientRegisterMessage } from 'Plugins/PatientAPI/PatientRegisterMessage'
import {RegisterMessage} from 'Plugins/DoctorAPI/RegisterMessage'
import {LoginMessage} from 'Plugins/DoctorAPI/LoginMessage'
import {sendPostRequest,ErrorModal} from 'Pages/ErrorMessage'
import './app.css'
import { Email } from '@mui/icons-material'

interface FormProps {
    title: string;
    fields: { name: string, type: string, label: string }[];
    createMessage: (formData: any) => API;
    onSuccess?: () => void;
}

const GenericForm: React.FC<FormProps> = ({ title, fields, createMessage, onSuccess }) => {
    const [formData, setFormData] = useState<any>({});
    const [error, setError] = useState<string | null>(null);
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
        sendPostRequest(message, setError, onSuccess);
        //console.log(formData);
    };

    const closeModal = () => {
        setError(null);
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
        </div>
    );
};
const StudentRegisters: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'Name',required:true },
        { name: 'email', type: 'email', label: 'email' ,required:true},
        { name: 'password', type: 'password', label: 'password',required:true },
        { name: 'number', type: 'textarea', label: 'number',required:true }
    ];

    const createMessage = (formData: any) => new PatientRegisterMessage(formData.name, formData.password,formData.email,formData.number);

    return <GenericForm title="Student Register" fields={fields} createMessage={createMessage} />;
};
const StudentLogins: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'name',required:true },
        { name: 'email', type: 'email', label: 'email', required: false },
        { name: 'password', type: 'password', label: 'password' ,required: true},
        { name: 'number', type: 'textarea', label: 'number',required: false }
    ];
    const history=useHistory()
    const createMessage = (formData: any) => new PatientLoginMessage(formData.name, formData.password);
    const handleSuccess = () => {
        history.push('/library');
    };
    return <GenericForm title="Student Login" fields={fields} createMessage={createMessage} onSuccess={handleSuccess} />;
};
const AdminRegisters: React.FC = () => {
    const fields = [
        { name: 'name', type: 'text', label: 'name',required:true },
        { name: 'email', type: 'email', label: 'email',required: true },
        { name: 'password', type: 'password', label: 'password',required: true },
        { name: 'number', type: 'textarea', label: 'number',required: true}
    ];

    const createMessage = (formData: any) => new RegisterMessage(formData.name, formData.password, formData.email, formData.number);
    return <GenericForm title="Administrator Register" fields={fields} createMessage={createMessage} />;
};

const AdminLogins: React.FC = () =>{
    const history=useHistory()
    const fields = [
        { name: 'name', type: 'text', label: 'Name',required:true },
        { name: 'email', type: 'email', label: 'Email',required: false },
        { name: 'password', type: 'password', label: 'password',required: true },
        { name: 'number', type: 'textarea', label: 'Number',required: false }
    ];
    const handleSuccess = () => {
        history.push('/admin');
    };
    const createMessage = (formData: any) => new PatientRegisterMessage(formData.name, formData.password,formData.email,formData.number);

    return <GenericForm title="Administrator Login" fields={fields} createMessage={createMessage} onSuccess={handleSuccess}  />;
};

export function StudentRegister() {
    return (
        <div style={{background:'aliceblue',opacity:'90%'}}>
            <StudentRegisters />
        </div>
    );
}

export function StudentLogin() {
    return (
        <div style={{background:'white',opacity:'90%'}}>
            <StudentLogins />
        </div>
    );
}

export function AdminRegister() {
    return (
        <div style={{background:'lightcyan',opacity:'90%',color:'blueviolet'}}>
            <AdminRegisters />
        </div>
    );
 }

export function AdminLogin() {
    return (
        <div style={{background:'lightyellow',opacity:'90%',color:'deepskyblue'}}>
            <AdminLogins />
        </div>
    );
}

