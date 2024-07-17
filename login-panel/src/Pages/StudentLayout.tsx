// src/Pages/StudentLayout.tsx
import React from 'react';
import { useHistory } from 'react-router-dom';

interface StudentLayoutProps {
    children: React.ReactNode;
}

const StudentLayout: React.FC<StudentLayoutProps> = ({ children }) => {
    const history = useHistory();

    return (
        <div style={containerStyle}>
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

const contentStyle: React.CSSProperties = {
    flex: '3',
    background: '#ffffff',
    padding: '20px',
    borderRadius: '10px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
};

export default StudentLayout;
