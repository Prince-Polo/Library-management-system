import React, { useState, useEffect } from 'react';
import { useHistory, useLocation } from 'react-router';
import { SeatReservationModal } from './SeatSelection';
import { useStore } from './store';
import {ErrorModal} from './ErrorMessage'
import { useSectionData, useSeatData, SectionInfo, SeatInfo } from 'Pages/LibraryDataLinking';
//import public.Images.*

export type LibrarySetting = {
    name: string;
    index: string;
    total: number;
    available: number;
};

export const AreaSelection: React.FC = () => {
    const location = useLocation<LibrarySetting>();
    const history = useHistory();
    const info = useStore((state) => state.info);
    const userName = info?.userName;
    const { name, index, total, available } = location.state || {
        name: 'Unknown Library',
        index: -1,
        total: 0,
        available: 0
    };
    const [selectedArea, setSelectedArea] = useState<string | null>(null);
    const [selectedSeat, setSelectedSeat] = useState<string | null>(null);
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isSeatOccupied, setIsSeatOccupied] = useState<boolean>(false);

    const { sections, fetchSections, error: sectionError, clearError: clearSectionError } = useSectionData();
    const { seats, fetchSeats, error: seatError, clearError: clearSeatError } = useSeatData();

    useEffect(() => {
        if (index !== -1) {
            fetchSections(index.toString());
        }
    }, [index, fetchSections]);

    const handleLogout = () => {
        console.log('Logged out');
        history.push('/');
    };

    const handleBack = () => {
        history.goBack();
    };

    const handleAreaClick = (area: string) => {
        setSelectedArea(area === selectedArea ? null : area);
        if (area !== selectedArea) {
            fetchSeats(index.toString(), area);
        }
    };

    const handleSeatClick = (seatNumber: string, section:string,occupied: boolean) => {
        console.log(`Seat ${section}${seatNumber} clicked by ${userName}`);
        setSelectedSeat(seatNumber);
        setIsSeatOccupied(occupied);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setSelectedArea(null);
        fetchSections(index.toString());
    };

    const getImage = () => {
        if (selectedArea) {
            return `/Images/F${index}${selectedArea}.png`;
        }
        return `/Images/F${index}.png`;
    };

    const sortedSeats = seats.slice().sort((a, b) => {
        const seatNumberA = parseInt(a.seatNumber.replace(/^\D+/g, ''), 10);
        const seatNumberB = parseInt(b.seatNumber.replace(/^\D+/g, ''), 10);
        return seatNumberA - seatNumberB;
    });

    return (
        <div style={containerStyle}>
            <div style={headerStyle}>
                <div style={titleStyle}>{`${name} (${index}) seat booking`}</div>
                <div style={infoStyle}>
                    <p>{`Total: ${total}`}</p>
                    <p>{`Available: ${available}`}</p>
                </div>
                <button
                    id="logout-button"
                    style={logoutButtonStyle}
                    onMouseEnter={e => (e.currentTarget.style.backgroundColor = 'orange')}
                    onMouseLeave={e => (e.currentTarget.style.backgroundColor = '#007bff')}
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </div>
            <div style={mainStyle}>
                <div style={areaStyle}>
                    {sections.map((section) => (
                        <div key={section.section}>
                            <div style={areaButtonStyle} onClick={() => handleAreaClick(section.section)}>
                                {`${section.section} > Total: ${section.totalSeats} Available: ${section.freeSeats}`}
                            </div>
                            {selectedArea === section.section && (
                                <div style={dropdownStyle}>
                                    {sortedSeats.map(seat => (
                                        <button key={seat.seatNumber} style={seatButtonStyle}
                                                onClick={() => handleSeatClick(seat.seatNumber, section.section,seat.occupied === 'true')}>
                                            <span>{index === 0 ? `G` : `F${index}`}{section.section}{seat.seatNumber.padStart(3,'0')}</span>
                                            <span style={{ color: seat.status === 'Confirmed' ? 'yellow' : seat.occupied === 'true' ? 'red' : 'green' }}>●</span>
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
                <div style={imageContainerStyle}>
                    <img src={getImage()} alt="Library Layout" style={imageStyle} />
                </div>
            </div>
            <button style={backButtonStyle} onClick={handleBack}>
                ↩
            </button>
            <SeatReservationModal
                isOpen={isModalOpen}
                library={name}
                floor={index.toString()}
                area={selectedArea || ''}
                seat={selectedSeat || ''}
                occupied={isSeatOccupied}
                onClose={handleCloseModal}
            />
            {sectionError && <ErrorModal message={sectionError} onClose={clearSectionError} />}
            {seatError && <ErrorModal message={seatError} onClose={clearSeatError} />}
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    position: 'relative',
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
};

const headerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px 20px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    zIndex: 2,
};

const titleStyle: React.CSSProperties = {
    fontSize: '24px',
    fontWeight: 'bold',
};

const infoStyle: React.CSSProperties = {
    textAlign: 'right',
};

const logoutButtonStyle: React.CSSProperties = {
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '10px 20px',
    cursor: 'pointer',
};

const mainStyle: React.CSSProperties = {
    display: 'flex',
    flex: 1,
};

const areaStyle: React.CSSProperties = {
    backgroundColor: '#e7e7e7',
    width: '30%',
    padding: '10px',
    display: 'flex',
    flexDirection: 'column',
};

const areaButtonStyle: React.CSSProperties = {
    backgroundColor: '#f0f0f0',
    padding: '10px',
    margin: '5px 0',
    cursor: 'pointer',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    zIndex: 3,
};

const dropdownStyle: React.CSSProperties = {
    maxHeight: '600px',
    overflowY: 'auto',
    padding: '10px',
    backgroundColor: '#fafafa',
    border: '1px solid #ddd',
    marginTop: '5px',
    zIndex: 1,
};

const seatButtonStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '5px 10px',
    backgroundColor: 'white',
    border: '1px solid #ddd',
    borderRadius: '5px',
    marginBottom: '1px',
    cursor: 'pointer',
    width: '100%',
    height: '30px',
};

const imageContainerStyle: React.CSSProperties = {
    position: 'fixed',
    width: '65%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    right: '0',
    top: '0',
    bottom: '0',
    zIndex: 1,
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
