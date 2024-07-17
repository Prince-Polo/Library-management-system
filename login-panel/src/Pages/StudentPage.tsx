import React,{useEffect} from 'react';
import { useHistory,useLocation } from 'react-router';
import {useStoreLib,LibraryInfo} from "./LibrarySetting";
import {useToMyCenter,useFloorData,FloorInfo} from 'Pages/LibraryDataLinking';
import {ErrorModal} from 'Pages/ErrorMessage'

const StudentPage: React.FC = () => {
    const history = useHistory();
    const setLibraryInfo = useStoreLib((state) => state.setLibraryInfo);
    const { floorData, fetchFloorData, error: floorDataError, clearError: clearFloorDataError } = useFloorData();

    useEffect(() => {
        fetchFloorData();
    }, []);

    const { toMyCenter, error,clearError } = useToMyCenter();

    const defaultLibraries = [
        { name: 'Animal Hunting and Herding Library (G)', total: 0, available: 0 },
        { name: 'Agriculture Library (F1)', total: 0, available: 0 },
        { name: 'Bovine Library (F2)', total: 0, available: 0 },
        { name: 'Bovine Library (F3)', total: 0, available: 0 },
        { name: 'Arts Library (F4)', total: 0, available: 0 },
        { name: 'Banteng Crafts Library (F5)', total: 0, available: 0 },
    ];

    const libraries=[...defaultLibraries];

    if (floorData) {
        floorData.floors.forEach((floor: FloorInfo) => {
            const floorIndex = parseInt(floor.floor) ;
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
        history.push({pathname:`/areas/${index}`,state});
    };

    return (
        <div style={containerStyle}>
            <div style={headerStyle}>
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
            <h1>Java Bantengs' Library Seat Management System</h1>
            <div style={librariesStyle}>
                {libraries.map((library, index) => (
                    <div key={index} style={libraryStyle}>
                        <h2>{library.name}</h2>
                        <p>Available: {library.available}, Total: {library.total}</p>
                        <button
                            style={buttonStyle}
                            onMouseEnter={handleMouseEnter}
                            onMouseLeave={handleMouseLeave}
                            onClick={() => {
                                 const libInfo: LibraryInfo = {name: library.name, index: index,available: library.available, total: library.total};
                                 setLibraryInfo(libInfo);
                                 handleNavigation(index, libInfo);
                            }}
                        >
                            Select Seats
                        </button>
                    </div>
                ))}
            </div>
            {error && <ErrorModal message={error} onClose={clearError}/>}
        </div>
    );
};
const containerStyle: React.CSSProperties = {
    padding: '20px',
    textAlign: 'center',
    position: 'relative'
};

const librariesStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'center'
};

const libraryStyle: React.CSSProperties = {
    border: '1px solid #ccc',
    margin: '10px',
    padding: '20px',
    width: '200px',
    textAlign: 'left',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between'
};

const buttonStyle: React.CSSProperties = {
    display: 'block',
    marginTop: '10px',
    padding: '10px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer'
};

const buttonHoverStyle: React.CSSProperties = {
    backgroundColor: '#0056b3'
};

const headerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'flex-end',
    alignItems: 'center',
    position: 'absolute',
    top: '10px',
    right: '10px'
};

export default StudentPage;
