import React from 'react';
import { useHistory } from 'react-router';
import myImage from '../Images/FrontPage.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import BrowseJobsPage from 'Pages/BrowseJobsPage'

export function AnotherPage() {
    const history = useHistory();

    const backgroundStyle: React.CSSProperties = {
        backgroundImage: `url(${myImage})`,
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

    const boxStyle: React.CSSProperties = {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        padding: '30px',
        borderRadius: '20px',
        display: 'flex',
        flexDirection: 'column' as const,
        alignItems: 'center',
        width: '80%',
    };

    const headerStyle: React.CSSProperties = {
        color: 'rgba(225,168,88,0.82)',
        marginBottom: '20px',
        fontFamily: 'Georgia, serif',
        fontSize: '2em',
        fontWeight: 'bold',
    };

    const textStyle: React.CSSProperties = {
        color: 'white',
        fontSize: '18px',
        fontFamily: 'Arial, sans-serif',
        marginBottom: '20px',
        textAlign: 'left',
    };

    const buttonStyle: React.CSSProperties = {
        cursor: 'pointer',
        fontFamily: 'Times New Roman, Times, serif',
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

    const iconStyle: React.CSSProperties = {
        marginBottom: '10px',
        fontSize: '40px',
    };

    return (
        <div style={backgroundStyle}>
            <div style={boxStyle}>
                <header style={headerStyle}>
                    <h1>About Java Banteng Library</h1>
                </header>
                <p style={textStyle}>
                    Welcome to the Java Banteng Library! Why Java Banteng, you ask? Well, we love a good pun, and what's better than combining the strength of a banteng (that's a wild cow for the uninitiated) with the versatility of Java? Here, we bring together the best of both worlds: a robust collection of books and resources, and a dynamic, engaging environment that fosters learning and creativity.
                </p>
                <p style={textStyle}>
                    Our library isn't just about books, though we do have plenty of those. It's a hub for community, a place where you can explore new ideas, collaborate on projects, and even catch a nap between classes (we won't tell). Whether you're here to study hard or hardly study, the Java Banteng Library is your go-to spot for all things academic and beyond.
                </p>
                <p style={textStyle}>
                    So, charge in like a banteng, grab a book, a bean bag, and maybe a cup of coffee, and make the Java Banteng Library your second home. Happy reading!
                </p>
                <button
                    style={buttonStyle}
                    onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = buttonPrimaryHoverStyle.backgroundColor!)}
                    onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = buttonStyle.backgroundColor!)}
                    onClick={() => history.push('/')}
                >
                    <FontAwesomeIcon icon={faArrowLeft} style={iconStyle} />
                    Back to Main
                </button>
            </div>
        </div>
    );
}

export default AnotherPage;
