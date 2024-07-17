import React, { useState } from 'react';
import { useHistory } from 'react-router';
import './Styles/app.css';
import myImage from '../assets/images/background.png'; // 根据你的文件夹结构调整路径

export function Main() {
    const history = useHistory();
    const backgroundStyle: React.CSSProperties = {
        backgroundImage: `url(${myImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        height: '100vh',
        display: 'flex',
        flexDirection: 'column' as 'column',  // 类型断言
        justifyContent: 'center',
        alignItems: 'center',
        color: '#fff',
        textAlign: 'center',
    };
    const [showModal, setShowModal] = useState(false);
    const [showModal1, setShowModal1] = useState(false);

    const handleRegisterClick = () => {
        setShowModal(true);
    };

    const handleRegisterClick1 = () => {
        setShowModal1(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setShowModal1(false);
    };

    return (
        <div className="App" style={backgroundStyle}>
            <header className="App-header">
                <h1>预约系统</h1>
            </header>
            <main style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <div className="button-group">
                    <button className="button button-student" onClick={handleRegisterClick}>
                        I am a student.
                    </button>
                    <button className="button button-admin" onClick={handleRegisterClick1}>
                        I am an administrator.
                    </button>
                    <button className="button button-about" onClick={() => history.push('/another')}>
                        About our library.
                    </button>
                </div>
            </main>
            {showModal && (
                <div className="overlay">
                    <div className="modal">
                        <h1 style={{ marginBottom: '20px' }}>Register/Login?</h1>
                        <div className="buttons">
                            <button className="button button-primary" onClick={() => history.push('/StudentRegister')}>
                                Register
                            </button>
                            <button className="button button-primary" onClick={() => history.push('/StudentLogin')}>
                                Login
                            </button>
                            <button className="button button-primary" onClick={handleCloseModal}>
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {showModal1 && (
                <div className="overlay">
                    <div className="modal">
                        <h1 style={{ marginBottom: '20px' }}>Register/Login?</h1>
                        <div className="buttons">
                            <button
                                className="button button-primary"
                                style={{ color: 'yellow', background: 'blueviolet' }}
                                onClick={() => history.push('/AdminRegister')}
                            >
                                Register
                            </button>
                            <button
                                className="button button-primary"
                                style={{ color: 'yellow', background: 'blueviolet' }}
                                onClick={() => history.push('/AdminLogin')}
                            >
                                Login
                            </button>
                            <button
                                className="button button-primary"
                                style={{ color: 'yellow', background: 'blueviolet' }}
                                onClick={handleCloseModal}
                            >
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

// 添加样式到 Styles/app.css 文件中

