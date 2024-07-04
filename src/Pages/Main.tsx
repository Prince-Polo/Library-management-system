import React, { ChangeEvent, FormEvent, useState } from 'react'
import axios, { isAxiosError } from 'axios'
import { API } from 'Plugins/CommonUtils/API'
import { LoginMessage } from 'Plugins/DoctorAPI/LoginMessage'
import { RegisterMessage } from 'Plugins/DoctorAPI/RegisterMessage'
import { PatientLoginMessage } from 'Plugins/PatientAPI/PatientLoginMessage'
import { PatientRegisterMessage } from 'Plugins/PatientAPI/PatientRegisterMessage'
import { AddPatientMessage } from 'Plugins/DoctorAPI/AddPatientMessage'
import { useHistory } from 'react-router';
import './app.css'
export function Main(){
    const history=useHistory()

    const sendPostRequest = async (message: API) => {
        try {
            const response = await axios.post(message.getURL(), JSON.stringify(message), {
                headers: { 'Content-Type': 'application/json' },
            });
            console.log('Response status:', response.status);
            console.log('Response body:', response.data);
        } catch (error) {
            if (isAxiosError(error)) {
                // Check if the error has a response and a data property
                if (error.response && error.response.data) {
                    console.error('Error sending request:', error.response.data);
                } else {
                    console.error('Error sending request:', error.message);
                }
            } else {
                console.error('Unexpected error:', error);
            }
        }
    };
    const [formData, setFormData] = useState({
        name: '',
        number: '',
        password: '',
        email: '',
    });

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e:FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log('Username:', formData.name);
        console.log('Password:', formData.password);
        console.log('Number:', formData.number);
        console.log('Email:',formData.email);
        // 在这里处理表单提交，例如发送数据到服务器
    };
    const [showModal, setShowModal] = useState(false);
    const [showModallogin, setShowModallogin]=useState(false)

    const handleRegisterClick = () => {
        setShowModal(true);
    };
    const handleLoginClick=()=>{
        setShowModallogin(true);
    }
    const handleCloseModal = () => {
        setShowModal(false);
        setShowModallogin(false);
    };
    return (
        <div className="App">
            <header className="App header">
                <h1>预约系统</h1>
            </header>
            <main style={{ display: 'block', justifyContent: 'space-around', alignItems: 'center', flexWrap: 'wrap' }}>
                <div className={'button-group'}>
                <button className={'button button-secondary'} onClick={() => sendPostRequest(new RegisterMessage('aaaa', 'bbbb'))}>
                    Administrator Register
                </button>
                <button className={'button button-secondary'} onClick={() => sendPostRequest(new LoginMessage('aaaab', 'bbbb'))}>
                    Administrator Login
                </button></div>
                <br />
                <div className={'button-group'}>
                <button className={'button button-secondary'} onClick={handleRegisterClick}>
                    Student Register
                </button>
                <button className={'button button-secondary'} onClick={handleLoginClick}>
                    Student Login
                </button></div>
                <br />
                <button className={'button button-secondary'}
                        onClick={() => sendPostRequest(new AddPatientMessage('aaaa', 'cccc'))}>
                    Add Student
                </button>
                <br />
                <div className={'button-group'}>
                <button className={'button button-secondary'} onClick={() => history.push('/another')}>
                    jump to another page
                </button>
                <button className={'button button-secondary'} onClick={() => history.push('/library')}>
                    To the library
                </button>
                </div>
            </main>
            {showModal && (
                <div style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    background: 'rgba(0, 0, 0, 0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                    <div style={{ background: 'lightcyan', padding: '20px', borderRadius: '10px' }}>
                        <h2>Register</h2>
                        <form onSubmit={handleSubmit} className="contact-form">
                            <div className="form-group">
                                <label htmlFor="name">Name:</label>
                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="email">Email:</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="message">Password:</label>
                                <textarea
                                    id="password"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    required
                                ></textarea>
                            </div>
                            <div className="form-group">
                            <label htmlFor="message">Number:</label>
                            <textarea
                                id="number"
                                name="number"
                                value={formData.number}
                                onChange={handleChange}
                                required
                            ></textarea>
                    </div>
                            <button className={'button button-primary'} type="submit" onClick={()=>sendPostRequest(new PatientRegisterMessage(formData.name,formData.password))}>Submit</button>
                        </form>
                        <button
                            className={'button button-primary'}
                            onClick={handleCloseModal}>Close</button>
                    </div>
                </div>
            )}
            {showModallogin && (
                <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', background: 'rgba(0, 0, 0, 0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                    <div style={{ background: 'lightcyan', padding: '20px', borderRadius: '10px' }}>
                        <h2>Login</h2>
                        <form onSubmit={handleSubmit} className="contact-form">
                            <div className="form-group">
                                <label htmlFor="name">Name:</label>
                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="email">Email:</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="message">Password:</label>
                                <textarea
                                    id="password"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    required
                                ></textarea>
                            </div>
                            <div className="form-group">
                                <label htmlFor="message">Number:</label>
                                <textarea
                                    id="number"
                                    name="number"
                                    value={formData.number}
                                    onChange={handleChange}
                                ></textarea>
                            </div>
                            <button className={'button button-primary'} type="submit" onClick={()=>sendPostRequest(new PatientLoginMessage(formData.name,formData.password))}>Submit</button>
                        </form>
                        <button
                            className={'button button-primary'}
                            onClick={handleCloseModal}>Close</button>
                    </div>
                </div>
            )}
        </div>
    );
}


