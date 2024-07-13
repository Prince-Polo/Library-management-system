import React from 'react';

const MyCenter: React.FC = () => {
    const myCenterStyle: React.CSSProperties = {
        textAlign: 'center',
    };

    const centerSectionStyle: React.CSSProperties = {
        margin: '20px',
    };

    return (
        <div style={myCenterStyle}>
            <h1>My Center</h1>
            <div style={centerSectionStyle}>
                <h2>Reservation History</h2>
                {/* List of reservations */}
            </div>
            <div style={centerSectionStyle}>
                <h2>Apply to Volunteer</h2>
                {/* Volunteer application form */}
            </div>
            <div style={centerSectionStyle}>
                <h2>Report an Issue</h2>
                {/* Issue reporting form */}
            </div>
        </div>
    );
};

export default MyCenter;