import React, { useEffect, useState } from 'react'
import { useHistory, useLocation } from 'react-router';
import { LibraryInfo } from './LibrarySetting';
import { useSectionData } from 'Pages/LibraryDataLinking';
import { ErrorModal } from 'Pages/ErrorMessage';

export const AreaSelection: React.FC = () => {
    const history = useHistory();
    const location = useLocation<LibraryInfo>();
    const { name, index } = location.state || { name: 'Unknown Library', index: -1 };
    const { sections, fetchSections, error: sectionError, clearError: clearSectionError } = useSectionData();
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

    useEffect(() => {
        if (index !== -1) {
            fetchSections(index.toString());
        }
    }, [index, fetchSections]);

    const handleNavigation = (area: string) => {
        history.push({ pathname: `/area/${index}/${area}`, state: { name, index, area } });
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div style={containerStyle}>
            <div style={headerStyle}>
                <h1>{name}</h1>
            </div>
            <div style={areasStyle}>
                {sections.map((section) => (
                    <div key={section.section} style={areaStyle}>
                        <h2>Section {section.section}</h2>
                        <p>Available: {section.freeSeats}, Total: {section.totalSeats}</p>
                        <button
                            style={buttonStyle}
                            onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = buttonHoverStyle.backgroundColor!)}
                            onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = buttonStyle.backgroundColor!)}
                            onClick={() => handleNavigation(section.section)}
                        >
                            Select Section
                        </button>
                    </div>
                ))}
            </div>
            <button style={backButtonStyle} onClick={() => history.push("/Student")}>
                ↩
            </button>
            {sectionError && <ErrorModal message={sectionError} onClose={clearSectionError} />}
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    padding: '20px',
    textAlign: 'center',
    position: 'relative',
};

const headerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px 20px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const areasStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'center',
};

const areaStyle: React.CSSProperties = {
    border: '1px solid #ccc',
    margin: '10px',
    padding: '20px',
    width: '200px',
    textAlign: 'left',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
};

const buttonStyle: React.CSSProperties = {
    display: 'block',
    marginTop: '10px',
    padding: '10px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
};

const buttonHoverStyle: React.CSSProperties = {
    backgroundColor: '#0056b3',
};

const backButtonStyle: React.CSSProperties = {
    position: 'absolute',
    bottom: '20px',
    right: '20px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '50%',
    width: '50px',
    height: '50px',
    fontSize: '24px',
    cursor: 'pointer',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 2,
};

