import React, { useState, useEffect, useCallback } from 'react';
import AdminLayout from './AdminLayout';

interface Seat {
    floor: string;
    section: string;
    seatNumber: string;
    status: string;
    feedback: string;
    occupied: string;
    studentNumber: string;
}

const CheckSeatsPage: React.FC = () => {
    const [seats, setSeats] = useState<Seat[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [feedback, setFeedback] = useState<string>('');
    const [selectedSeat, setSelectedSeat] = useState<Seat | null>(null);
    const [showConfirmModal, setShowConfirmModal] = useState<boolean>(false);
    const [filterStatus, setFilterStatus] = useState<string>('All');

    const fetchReportedSeats = useCallback(async () => {
        const message = {
            type: 'ReportedSeatsQuery'
        };
        try {
            const response = await fetch('http://127.0.0.1:10004/api/Seat/ReportedSeatQueryMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });
            if (!response.ok) {
                throw new Error('Failed to fetch reported seats');
            }
            let result = await response.text();
            console.log(result);
            let parsedResult;
            try {
                parsedResult = JSON.parse(result);
                parsedResult = JSON.parse(parsedResult); // 第二次解析
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }
            console.log(parsedResult); // 检查数据格式
            setSeats(parsedResult.seats);
        } catch (error) {
            setError(error.message);
        }
    }, []);

    useEffect(() => {
        fetchReportedSeats();
    }, [fetchReportedSeats]);

    const getFeedbackContent = (feedback: string) => {
        const startIndex = feedback.indexOf("following issues:");
        return startIndex !== -1 ? feedback.substring(startIndex + "following issues:".length).trim() : feedback;
    };

    const handleConfirm = async (seat: Seat) => {
        setSelectedSeat(seat);
    };

    const handleSubmitFeedback = async () => {
        if (!selectedSeat) return;

        const message = {
            floor: selectedSeat.floor,
            section: selectedSeat.section,
            seatNumber: selectedSeat.seatNumber,
            feedback: feedback
        };

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Seat/SeatConfirmMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            if (!response.ok) {
                throw new Error('Failed to confirm seat');
            }

            setFeedback('');
            setSelectedSeat(null);
            await fetchReportedSeats(); // 重新获取座位信息以更新列表
        } catch (error) {
            setError(error.message);
        }
    };

    const handleRefresh = async (seat: Seat) => {
        setSelectedSeat(seat);
        setShowConfirmModal(true);
    };

    const confirmRefresh = async () => {
        if (!selectedSeat) return;

        const message = {
            floor: selectedSeat.floor,
            section: selectedSeat.section,
            seatNumber: selectedSeat.seatNumber
        };

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Seat/SeatRefreshMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            if (!response.ok) {
                throw new Error('Failed to refresh seat');
            }

            await fetchReportedSeats(); // 重新获取座位信息以更新列表
        } catch (error) {
            setError(error.message);
        }

        setShowConfirmModal(false);
        setSelectedSeat(null);
    };

    const filteredSeats = seats.filter(seat => filterStatus === 'All' || seat.status === filterStatus);

    return (
        <AdminLayout>
            <h2 style={headingStyle}>Reported Seats</h2>
            {error && <p style={errorMessageStyle}>{error}</p>}
            <div style={filterContainerStyle}>
                <label htmlFor="statusFilter" style={filterLabelStyle}>Filter by Status:</label>
                <select
                    id="statusFilter"
                    value={filterStatus}
                    onChange={(e) => setFilterStatus(e.target.value)}
                    style={filterSelectStyle}
                >
                    <option value="All">All</option>
                    <option value="Reported">Reported</option>
                    <option value="Confirmed">Confirmed</option>
                </select>
            </div>
            <table style={tableStyle}>
                <thead>
                <tr>
                    <th style={thStyle}>Floor</th>
                    <th style={thStyle}>Section</th>
                    <th style={thStyle}>Seat Number</th>
                    <th style={thStyle}>Status</th>
                    <th style={thStyle}>Occupied</th>
                    <th style={thStyle}>Feedback</th>
                    <th style={thStyle}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {filteredSeats.map((seat, index) => (
                    <tr key={index} style={trStyle}>
                        <td style={tdStyle}>{seat.floor}</td>
                        <td style={tdStyle}>{seat.section}</td>
                        <td style={tdStyle}>{seat.seatNumber}</td>
                        <td style={tdStyle}>{seat.status}</td>
                        <td style={tdStyle}>{seat.occupied}</td>
                        <td style={tdStyle}>{getFeedbackContent(seat.feedback)}</td>
                        <td style={tdStyle}>
                            {seat.status === 'Confirmed' ? (
                                <button onClick={() => handleRefresh(seat)} style={buttonStyle}>Refresh</button>
                            ) : (
                                <>
                                    <button onClick={() => handleConfirm(seat)} style={buttonStyle}>✔</button>
                                    <button onClick={() => handleRefresh(seat)} style={buttonStyle}>✖</button>
                                </>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            {selectedSeat && !showConfirmModal && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Submit Feedback for Seat {selectedSeat.seatNumber}</h3>
                        <textarea
                            value={feedback}
                            onChange={(e) => setFeedback(e.target.value)}
                            style={textareaStyle}
                        />
                        <button onClick={handleSubmitFeedback} style={submitButtonStyle}>Submit</button>
                        <button onClick={() => setSelectedSeat(null)} style={cancelButtonStyle}>Cancel</button>
                    </div>
                </div>
            )}
            {showConfirmModal && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Are you sure you want to refresh seat {selectedSeat?.seatNumber}?</h3>
                        <button onClick={confirmRefresh} style={submitButtonStyle}>Confirm</button>
                        <button onClick={() => { setShowConfirmModal(false); setSelectedSeat(null); }} style={cancelButtonStyle}>Cancel</button>
                    </div>
                </div>
            )}
        </AdminLayout>
    );
};

const headingStyle: React.CSSProperties = {
    marginBottom: '20px',
    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif',
    color: '#333',
};

const tableStyle: React.CSSProperties = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px',
};

const thStyle: React.CSSProperties = {
    border: '1px solid #ddd',
    padding: '8px',
    background: '#f0f8ff',
    fontWeight: 'bold',
};

const trStyle: React.CSSProperties = {
    cursor: 'pointer',
};

const tdStyle: React.CSSProperties = {
    border: '1px solid #ddd',
    padding: '8px',
};

const buttonStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    color: '#007bff',
    cursor: 'pointer',
    fontSize: '18px',
    margin: '0 5px',
};

const errorMessageStyle: React.CSSProperties = {
    color: 'red',
    fontWeight: 'bold',
};

const modalStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    background: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const modalContentStyle: React.CSSProperties = {
    background: 'white',
    padding: '20px',
    borderRadius: '10px',
    textAlign: 'center',
};

const textareaStyle: React.CSSProperties = {
    width: '100%',
    height: '100px',
    margin: '10px 0',
    padding: '10px',
    fontSize: '16px',
    borderRadius: '5px',
    border: '1px solid #ddd',
};

const submitButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px',
};

const cancelButtonStyle: React.CSSProperties = {
    background: '#ff6347',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px',
};

const filterContainerStyle: React.CSSProperties = {
    marginBottom: '20px',
    display: 'flex',
    alignItems: 'center',
};

const filterLabelStyle: React.CSSProperties = {
    marginRight: '10px',
    fontWeight: 'bold',
};

const filterSelectStyle: React.CSSProperties = {
    padding: '5px',
    borderRadius: '5px',
    border: '1px solid #ddd',
};

export default CheckSeatsPage;
