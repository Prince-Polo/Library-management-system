import React from 'react';
import { useHistory } from 'react-router';
import images from 'Pages/SharedComponents/ImageLoader';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUserGraduate,faUserShield,faBuildingColumns } from '@fortawesome/free-solid-svg-icons'

export function Main() {
    const history = useHistory();


    return (
        <div style={backgroundStyle}>
            <div style={boxStyle}>
                <header style={headerStyle}>
                    <h1>Java Bantengs' Library Management System</h1>
                </header>
                <main style={buttonGroupStyle}>
                    <button
                        style={buttonStyle}
                        onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = buttonPrimaryHoverStyle.backgroundColor!)}
                        onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = buttonStyle.backgroundColor!)}
                        onClick={()=>history.push('/StudentLogin')}
                    >
                        <FontAwesomeIcon icon={faUserGraduate} style={iconStyle} />
                        I am a student.
                    </button>
                    <button
                        style={buttonStyle}
                        onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = buttonPrimaryHoverStyle.backgroundColor!)}
                        onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = buttonStyle.backgroundColor!)}
                        onClick={()=>history.push('/AdminLogin')}
                    >
                        <FontAwesomeIcon icon={faUserShield} style={iconStyle} />
                        I am an administrator.
                    </button>
                    <button
                        style={buttonStyle}
                        onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = buttonPrimaryHoverStyle.backgroundColor!)}
                        onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = buttonStyle.backgroundColor!)}
                        onClick={() => history.push('/another')}
                    >
                        <FontAwesomeIcon icon={faBuildingColumns} style={iconStyle} />
                        About our library.
                    </button>
                </main>
            </div>
        </div>
    );
}

const backgroundStyle: React.CSSProperties = {
    backgroundImage: `url(${images['FrontPage.png']})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    height: '100vh',
    display: 'flex',
    flexDirection: 'column' as const,
    justifyContent: 'center',
    alignItems: 'center',
    color: '#fff',
    textAlign: 'center',
};

const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1100,
};

const modalStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'lightcyan',
    padding: '20px',
    borderRadius: '18px',
    flexDirection: 'column' as const,
    zIndex: 1200,
    height: '40%',
    width: '40%',
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

const buttonStyle: React.CSSProperties = {
    cursor: 'pointer',
    fontFamily: "Times New Roman, Times, serif",
    textAlign: 'center',
    display: 'flex',
    flexDirection: 'column' as const,
    alignItems: 'center',
    justifyContent: 'center',
    boxSizing: 'border-box',
    padding: '20px',
    backgroundColor: '#6A5ACD',
    color: 'white',
    border: 'none',
    height: '100px',
    fontSize: '20px',
    borderRadius: '5px',
    transition: 'background-color 0.3s ease',
    marginBottom: '10px',
    width: '30%',
    margin: '0 10px',
};

const buttonPrimaryHoverStyle: React.CSSProperties = {
    backgroundColor: '#483D8B',
};

const buttonGroupStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'row' as const, // 改为横向排列
    gap: '10px',
    alignItems: 'center',
    width: '100%',
    justifyContent: 'center', // 居中显示
};

const headerStyle: React.CSSProperties = {
    color: 'rgba(225,168,88,0.82)',
    marginBottom: '20px',
    fontFamily: "Georgia, serif",
    fontSize: '2em',
    fontWeight: 'bold',
};

const boxStyle: React.CSSProperties = {
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    padding: '30px',
    borderRadius: '20px',
    display: 'flex',
    flexDirection: 'column' as const,
    alignItems: 'center',
    width: '80%',
};

const iconStyle: React.CSSProperties = {
    marginBottom: '10px',
    fontSize: '40px',
};
