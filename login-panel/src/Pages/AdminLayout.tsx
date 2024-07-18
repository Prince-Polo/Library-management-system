import React, { ReactNode } from 'react';
import { useHistory } from 'react-router';

interface AdminLayoutProps {
    children: ReactNode;
}

const AdminLayout: React.FC<AdminLayoutProps> = ({ children }) => {
    const history = useHistory();

    return (
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
                <button style={buttonStyle} onClick={() => history.push('/AdminLogin')}>
                    Logout
                </button>
            </div>
            <div style={contentStyle}>
                {children}
            </div>
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    display: 'flex',
    background: '#f0f8ff',
    border: '1px solid #ddd',
    padding: '20px',
    borderRadius: '10px',
    maxWidth: '900px',
    margin: '20px auto',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
};

const sidebarStyle: React.CSSProperties = {
    flex: '1',
    marginRight: '20px',
    display: 'flex',
    flexDirection: 'column',
};

const contentStyle: React.CSSProperties = {
    flex: '3',
    background: '#ffffff',
    padding: '20px',
    borderRadius: '10px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
};

const buttonStyle: React.CSSProperties = {
    display: 'block',
    width: '100%',
    padding: '10px',
    margin: '10px 0',
    textAlign: 'center',
    background: '#87ceeb',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'background 0.3s',
};

export default AdminLayout;