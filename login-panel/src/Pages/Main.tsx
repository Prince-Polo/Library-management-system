import React, { useState } from 'react'
import myImage from './background.png'
import { useHistory } from 'react-router';
import './app.css'

export function Main(){
    const history=useHistory()
    const backgroundStyle={
        backgroundImage:`url(${myImage})`,
        backgroundSize:'cover',
        backgroundPosition:'center',
        backgroundRepeat:'repeat'
    }
    const [showModal, setShowModal] = useState(false);
    const [showModal1,setShowModal1]=useState(false);
    const handleRegisterClick = () => {
        setShowModal(true);
    };
    const handleRegisterClick1=()=>{
        setShowModal1(true)
    }
    const handleCloseModal = () => {
        setShowModal(false);
        setShowModal1(false);
    };
    return (
        <div className="App" style={backgroundStyle}>
            <header className="App header">
                <h1>预约系统</h1>
            </header>
            <main style={{ display: 'block', justifyContent: 'space-around', alignItems: 'center', flexWrap: 'wrap' }}>

                <div className={'button-group'}>
                <button className={'button button-secondary'} onClick={handleRegisterClick}>
                    I am a student.
                </button>
                <button className={'button button3'} onClick={handleRegisterClick1}>
                    I am an administrator.
                </button>
                <button className={'button button4'} onClick={() => history.push('/admin')}>
                    About our library.
                </button>
                </div>
            </main>
            {
                showModal && (
                    <div className="overlay">
                        <div className={'modal'}>
                            <h1 style={{ display: 'block',marginBottom:'20px',width:"100%"}}>Register/Login?</h1>
                         <div className={'buttons'}>
                            <button className={'button button-primary'}
                                    onClick={() => history.push("/StudentRegister")}>Register
                            </button>
                            <button className={'button button-primary'}
                                    onClick={() => history.push("/StudentLogin")}>Login
                            </button>
                            <button
                                className={'button button-primary'}
                                onClick={handleCloseModal}>Close
                            </button>
                        </div>
                    </div>
                    </div>
                )
            }
            {
                showModal1 && (
                    <div className="overlay">
                        <div className={'modal'}>
                            <h1 style={{ display: 'block',marginBottom:'20px',width:"100%"}}>Register/Login?</h1>
                            <div className={'buttons'}>
                                <button className={'button button-primary'} style={{color:'yellow',background:'blueviolet'}}
                                        onClick={() => history.push("/AdminRegister")}>Register
                                </button>
                                <button className={'button button-primary'} style={{color:'yellow',background:'blueviolet'}}
                                        onClick={() => history.push("/AdminLogin")}>Login
                                </button>
                                <button
                                    className={'button button-primary'}
                                    style={{color:'yellow',background:'blueviolet'}}
                                    onClick={handleCloseModal}>Close
                                </button>
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    );
}


