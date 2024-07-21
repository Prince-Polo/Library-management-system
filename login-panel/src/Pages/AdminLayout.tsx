import React, { ReactNode } from 'react';
import { useHistory } from 'react-router';
import myImage from '../Images/FrontPage.png'; // Replace with the correct path to your uploaded image

interface AdminLayoutProps {
    children: ReactNode;
}

const AdminLayout: React.FC<AdminLayoutProps> = ({ children }) => {
    const history = useHistory();

    return (
        <div style={backgroundStyle}>
            <div style={containerStyle}>
                <div style={sidebarStyle}>
                    <button style={buttonStyle} onClick={() => history.push('/AddJob')}>
                        Add Job
                    </button>
                    <button style={buttonStyle} onClick={() => history.push('/BrowseJobs')}>
                        Browse Jobs
                    </button>
                    <button style={buttonStyle} onClick={() => history.push('/CheckSeats')}>
                        Check Seats
                    </button>
                    <button style={buttonStyle} onClick={() => history.push('/ViewSeats')}>
                        View Seats
                    </button>
                    <button style={logoutButtonStyle} onClick={() => history.push('/AdminLogin')}>
                        Logout
                    </button>
                </div>
                <div style={contentContainerStyle}>
                    <div style={contentStyle}>
                        {children}
                    </div>
                </div>
            </div>
        </div>
    );
};

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

const containerStyle: React.CSSProperties = {
    display: 'flex',
    background: 'rgba(255, 255, 255, 0.85)', // Adjusted semi-transparent background for better readability
    border: '1px solid #ddd',
    padding: '20px',
    borderRadius: '10px',
    maxWidth: '1100px', // Adjusted width to move left and give more space
    width: '85%', // Ensure the container takes full width
    margin: '20px auto', // Adjusted margin to center the container
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    height: '80vh',
    overflow: 'hidden',
};

const sidebarStyle: React.CSSProperties = {
    flex: '0 10px 100px', // Fixed width for the sidebar
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center', // Center align buttons horizontally
    justifyContent: 'flex-start', // Align buttons to the top
    paddingTop: '20px', // Add some top padding for spacing
    position: 'relative', // Required for absolute positioning of logout button
    marginRight: '20px', // Add space between sidebar and content
};

const contentContainerStyle: React.CSSProperties = {
    flex: '1',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
};

const contentStyle: React.CSSProperties = {
    width: '100%',
    height: '100%',
    background: 'rgba(255, 255, 255, 0.95)', // Adjusted background for better readability
    padding: '10px',
    borderRadius: '10px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    overflowY: 'auto',
    color: '#333', // Dark text color for better readability
    fontWeight: 'bold', // Make text bold
};

const buttonStyle: React.CSSProperties = {
    width: '150px', // Fixed width for equal size
    height: '50px', // Fixed height for equal size
    margin: '10px 0',
    textAlign: 'center',
    background: '#6A5ACD',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'background 0.3s',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold', // Make text bold
};

const logoutButtonStyle: React.CSSProperties = {
    ...buttonStyle,
    position: 'absolute',
    bottom: '20px', // Fixed position to the bottom
};

export default AdminLayout;
