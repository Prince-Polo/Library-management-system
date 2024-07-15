import React, { useState } from 'react';
import myImage from './background.png'; // 将此行替换为您上传的图片路径
import { useHistory } from 'react-router';
import './app.css';
import { CSSProperties } from 'react';

export function Main() {
    const history = useHistory();
    const backgroundStyle: CSSProperties = {
        backgroundImage: `url(${myImage})`,
        backgroundSize: 'contain',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        width: '100%',
        height: '100%',
        maxWidth: '900px', // 限制最大宽度
        maxHeight: '1600px', // 限制最大高度，确保9:16比例
        position: 'relative',
    };
    const contentStyle: CSSProperties = {
        position: 'absolute',
        top: '0',
        left: '0',
        width: '100%',
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        alignItems: 'center',
        backgroundColor: 'transparent',
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
        <div className="App" style={{ width: '100vw', height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center', overflow: 'hidden' }}>
            <div style={backgroundStyle}>
                <div style={contentStyle}>
                    <header className="App-header">
                        <div className="title-container">
                            <div className="title-box">
                                <h1>华清图书馆</h1>
                            </div>
                        </div>
                    </header>
                    <main className="App-main">
                        <div className="button-group">
                            <button className="button button-primary" onClick={handleRegisterClick}>
                                我是学生
                            </button>
                            <button className="button button-primary" onClick={handleRegisterClick1}>
                                我是管理员
                            </button>
                            <button className="button button-secondary" onClick={() => history.push('/another')}>
                                关于图书馆
                            </button>
                        </div>
                    </main>
                </div>
                {showModal && (
                    <div className="overlay">
                        <div className="modal">
                            <h1>注册/登录?</h1>
                            <div className="buttons">
                                <button className="button button-primary" onClick={() => history.push("/StudentRegister")}>
                                    注册
                                </button>
                                <button className="button button-primary" onClick={() => history.push("/StudentLogin")}>
                                    登录
                                </button>
                                <button className="button button-secondary" onClick={handleCloseModal}>
                                    关闭
                                </button>
                            </div>
                        </div>
                    </div>
                )}
                {showModal1 && (
                    <div className="overlay">
                        <div className="modal">
                            <h1>注册/登录?</h1>
                            <div className="buttons">
                                <button className="button button-primary" onClick={() => history.push("/AdminRegister")}>
                                    注册
                                </button>
                                <button className="button button-primary" onClick={() => history.push("/AdminLogin")}>
                                    登录
                                </button>
                                <button className="button button-secondary" onClick={handleCloseModal}>
                                    关闭
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
