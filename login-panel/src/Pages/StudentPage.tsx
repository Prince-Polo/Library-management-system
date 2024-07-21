import React, { useEffect } from 'react';
import { useHistory } from 'react-router';
import { useStoreLib, LibraryInfo } from './LibrarySetting';
import { useToMyCenter, useFloorData, FloorInfo } from 'Pages/LibraryDataLinking';
import { ErrorModal } from 'Pages/ErrorMessage';
import images from 'Pages/ImageLoader'

const StudentPage: React.FC = () => {
    const history = useHistory();
    const setLibraryInfo = useStoreLib((state) => state.setLibraryInfo);
    const { floorData, fetchFloorData, error: floorDataError, clearError: clearFloorDataError } = useFloorData();

    useEffect(() => {
        fetchFloorData();
    }, []);

    const { toMyCenter, err, clearToMyCenterError } = useToMyCenter();

    const defaultLibraries = [
        { name: 'Animal Hunting and Herding Library (G)', total: 0, available: 0 },
        { name: 'Agriculture Library (F1)', total: 0, available: 0 },
        { name: 'Early Settlements Library (F2)', total: 0, available: 0 },
        { name: 'Bovine Library (F3)', total: 0, available: 0 },
        { name: 'Arts Library (F4)', total: 0, available: 0 },
        { name: 'Banteng Crafts Library (F5)', total: 0, available: 0 },
    ];

    const libraries = [...defaultLibraries];

    if (floorData) {
        floorData.floors.forEach((floor: FloorInfo) => {
            const floorIndex = parseInt(floor.floor);
            if (floorIndex >= 0 && floorIndex < libraries.length) {
                libraries[floorIndex] = {
                    name: defaultLibraries[floorIndex].name,
                    total: parseInt(floor.totalSeats),
                    available: parseInt(floor.freeSeats),
                };
            }
        });
    }

    const handleMouseEnter = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        (e.target as HTMLButtonElement).style.backgroundColor = buttonHoverStyle.backgroundColor!;
    };

    const handleMouseLeave = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        (e.target as HTMLButtonElement).style.backgroundColor = buttonStyle.backgroundColor!;
    };

    const handleNavigation = (index: number, state: LibraryInfo) => {
        history.push({ pathname: `/areas/${index}`, state });
    };

    return (
        <div style={containerStyle}>
            <div style={headerContainerStyle}>
                <h1 style={headerStyle}>Java Bantengs' Library Seat Management System</h1>
                <div style={buttonContainerStyle}>
                    <button
                        style={buttonStyle}
                        onMouseEnter={handleMouseEnter}
                        onMouseLeave={handleMouseLeave}
                        onClick={toMyCenter}
                    >
                        My Center
                    </button>
                    <button
                        style={{ ...buttonStyle, marginLeft: '10px' }}
                        onMouseEnter={handleMouseEnter}
                        onMouseLeave={handleMouseLeave}
                        onClick={() => history.push('/')}
                    >
                        Logout
                    </button>
                </div>
            </div>
            <div style={librariesStyle}>
                {libraries.map((library, index) => (
                    <div key={index} style={libraryStyle}>
                        <img src={images[`F${index}Page.png`]} alt={`F${index}`} style={imageStyle} />
                        <h2>{library.name}</h2>
                        <p>Total: {library.total}, Available: {library.available}</p>
                        <button
                            style={buttonStyle}
                            onMouseEnter={handleMouseEnter}
                            onMouseLeave={handleMouseLeave}
                            onClick={() => {
                                const libInfo: LibraryInfo = { name: library.name, index: index, available: library.available, total: library.total };
                                setLibraryInfo(libInfo);
                                handleNavigation(index, libInfo);
                            }}
                        >
                            Select Seats
                        </button>
                    </div>
                ))}
            </div>
            {err && <ErrorModal message={err} onClose={clearToMyCenterError} />}
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    padding: '20px',
    textAlign: 'center',
    position: 'relative',
    fontFamily: 'Georgia, serif',
};

const headerContainerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '20px',
    borderRadius: '10px',
    marginBottom: '20px',
    textAlign: 'center',
};

const headerStyle: React.CSSProperties = {
    margin: 0,
    fontSize: '2.7em',
};

const buttonContainerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    marginTop: '40px',
};

const librariesStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'center',
    gap: '20px',
};

const libraryStyle: React.CSSProperties = {
    border: '1px solid #ccc',
    margin: '10px',
    padding: '15px',
    width: '27.5%',
    textAlign: 'center',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    borderRadius: '10px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

const buttonStyle: React.CSSProperties = {
    padding: '12px 20px',
    backgroundColor: '#6A5ACD',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    marginRight:'20px',
    transition: 'background-color 0.3s ease',
    textAlign: 'center',
    fontSize: '1em',
};

const buttonHoverStyle: React.CSSProperties = {
    backgroundColor: '#483D8B',
};

const imageStyle: React.CSSProperties = {
    width: '100%',
    aspectRatio: '3 / 2',
    objectFit: 'cover',
    borderRadius: '10px',
};

export default StudentPage;
