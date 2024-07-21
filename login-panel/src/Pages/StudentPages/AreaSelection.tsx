import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router';
import { LibraryInfo } from 'Plugins/GlobalVariables/LibrarySetting';
import { useSectionData } from 'Pages/SharedComponents/LibraryDataLinking';
import { ErrorModal } from 'Pages/SharedComponents/ErrorMessage';
import images from 'Pages/SharedComponents/ImageLoader';

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
            <div style={mainStyle}>
                <div style={areasStyle}>
                    {sections.map((section) => (
                        <div key={section.section} style={areaStyle}>
                            <h2>Section {section.section}</h2>
                            <p>Total: {section.totalSeats}, Available: {section.freeSeats}</p>
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
                <div style={imageContainerStyle}>
                    <img src={images[`F${index}.png`]||images[`cracking.png`]} alt={`F${index}`} style={imageStyle} />
                </div>
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
    height: '100vh',
    display: 'flex',
    flexDirection: 'column',
    background: 'rgba(245, 245, 245, 0.9)',
    fontFamily: 'Georgia, serif',
};

const headerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '20px',
    borderRadius: '10px',
    marginBottom: '10px',
    textAlign: 'center',
};

const mainStyle: React.CSSProperties = {
    display: 'flex',
    flex: 1,
};

const areasStyle: React.CSSProperties = {
    width: '40%',
    padding: '10px',
    display: 'flex',
    flexDirection: 'column',
    //overflowY: 'auto',
    borderRight: '1px solid #ccc',
    marginRight: '20px',
    maxHeight: 'calc(100vh - 160px)',
};

const areaStyle: React.CSSProperties = {
    border: '1px solid #ccc',
    margin: '10px',
    padding: '10px',
    textAlign: 'left',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    borderRadius: '10px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

const buttonStyle: React.CSSProperties = {
    marginTop: '10px',
    padding: '10px',
    backgroundColor: '#6A5ACD',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '1em',
    transition: 'background-color 0.3s ease',
    width: '80%',
    alignSelf: 'center',
};

const buttonHoverStyle: React.CSSProperties = {
    backgroundColor: '#483D8B',
};

const imageContainerStyle: React.CSSProperties = {
    width: '60%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const imageStyle: React.CSSProperties = {
    maxWidth: '100%',
    maxHeight: '100%',
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

export default AreaSelection;
