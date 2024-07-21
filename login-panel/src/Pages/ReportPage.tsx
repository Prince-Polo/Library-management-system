import React, { useState } from 'react';
import { useHistory } from 'react-router';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import { useStoreSelected } from 'Pages/LibrarySetting';
import { SeatReportMessage } from 'Plugins/SeatAPI/SeatReportMessage';
import myImage from '../Images/FrontPage.png'; // 请根据你的路径调整

export const ReportIssue: React.FC = () => {
    const history = useHistory();
    const selected = useStoreSelected((state) => state.library);
    const { library, floor, area, seat } = selected;
    const [checkedOptions, setCheckedOptions] = useState<{ [key: string]: boolean }>({
        seatDamage: false,
        deskDamage: false,
        electricalIssue: false,
        other: false,
    });

    const [otherIssueText, setOtherIssueText] = useState('');
    const [deskText, setDeskText] = useState('');
    const [seatText, setSeatText] = useState('');
    const [electricalText, setElectricalText] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const checkString = (inputString: string) => {
        return inputString.charCodeAt(0) - 48;
    }
    const floornum = checkString(floor);

    const handleCheckboxChange = (option: string) => {
        setCheckedOptions(prevState => ({ ...prevState, [option]: !prevState[option] }));
    };

    const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setOtherIssueText(e.target.value);
    };

    const handleSeatChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setSeatText(e.target.value);
    }

    const handleDeskChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setDeskText(e.target.value);
    }

    const handleElectricalChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setElectricalText(e.target.value);
    }

    const handleSubmit = () => {
        const reportData = `At ${library}, floor ${floor === "0" ? "G" : floor} area ${area}, seat ${floor === "0" ? "G" : floor}${area}${seat}, there are the following issues:${checkedOptions.seatDamage ? ` Seat damages: ${seatText}` : ""}${checkedOptions.deskDamage ? ` Desk damages: ${deskText}` : ""}${checkedOptions.electricalIssue ? ` Electrical issues: ${electricalText}` : ""}${checkedOptions.other ? ` Other issues: ${otherIssueText}` : ''}`;
        const message = new SeatReportMessage(floor, area, seat, reportData);

        console.log(floornum);
        sendPostRequest(message, setError, (response) => {
            setSuccess('Feedback submitted successfully!');
            setTimeout(() => {
                setSuccess(null);
                history.push('/Student')
            }, 2000);
        });
    };

    const closeModal = () => {
        setError(null);
    };

    const closeSuccessModal = () => {
        setSuccess(null);
    };

    return (
        <div style={containerStyle}>
            <div style={overlayStyle}>
                <div style={boxStyle}>
                    <div style={headerStyle}>
                        <button style={backButtonStyle} onClick={() => history.push('/Student')}>
                            &lt;
                        </button>
                        <h2 style={titleStyle}>Report an Issue</h2>
                    </div>
                    <h3 style={subTitleStyle}>Report an issue for seat {seat} in area {area} on floor {floor} of {library} library</h3>
                    <div style={checkboxContainerStyle}>
                        <div style={inputContainerStyle}>
                            <label style={inputLabelStyle}>
                                <span style={checkboxLabelStyle}>Seat Damage</span>
                                <input type="checkbox" checked={checkedOptions.seatDamage} onChange={() => handleCheckboxChange('seatDamage')} />
                            </label>
                            {checkedOptions.seatDamage && (
                                <textarea
                                    style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                                    rows={5}
                                    value={seatText}
                                    onChange={handleSeatChange}
                                />
                            )}
                        </div>
                        <div style={inputContainerStyle}>
                            <label style={inputLabelStyle}>
                                <span style={checkboxLabelStyle}>Desk Damage</span>
                                <input type="checkbox" checked={checkedOptions.deskDamage} onChange={() => handleCheckboxChange('deskDamage')} />
                            </label>
                            {checkedOptions.deskDamage && (
                                <textarea
                                    style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                                    rows={5}
                                    value={deskText}
                                    onChange={handleDeskChange}
                                />
                            )}
                        </div>
                        <div style={inputContainerStyle}>
                            <label style={inputLabelStyle}>
                                <span style={checkboxLabelStyle}>Electrical Issue</span>
                                <input type="checkbox" checked={checkedOptions.electricalIssue} onChange={() => handleCheckboxChange('electricalIssue')} />
                            </label>
                            {checkedOptions.electricalIssue && (
                                <textarea
                                    style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                                    rows={5}
                                    value={electricalText}
                                    onChange={handleElectricalChange}
                                />
                            )}
                        </div>
                        <div style={inputContainerStyle}>
                            <label style={inputLabelStyle}>
                                <span style={checkboxLabelStyle}>Other</span>
                                <input type="checkbox" checked={checkedOptions.other} onChange={() => handleCheckboxChange('other')} />
                            </label>
                            {checkedOptions.other && (
                                <textarea
                                    style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                                    rows={5}
                                    value={otherIssueText}
                                    onChange={handleTextareaChange}
                                />
                            )}
                        </div>
                    </div>
                    <button style={submitButtonStyle} onClick={handleSubmit}>
                        Submit
                    </button>
                </div>
            </div>
            <ErrorModal message={error} onClose={closeModal} />
            <SuccessModal message={success} onClose={closeSuccessModal} />
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    width: '100%',
    backgroundImage: `url(${myImage})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
};

const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1100,
};

const boxStyle: React.CSSProperties = {
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    padding: '40px',
    borderRadius: '10px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    textAlign: 'center',
    width: '60%',
};

const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px',
    fontSize: '24px',
    borderRadius:'10px',
    marginBottom: '20px',
};

const backButtonStyle: React.CSSProperties = {
    backgroundColor: 'transparent',
    border: 'none',
    color: 'white',
    fontSize: '24px',
    cursor: 'pointer',
};

const titleStyle: React.CSSProperties = {
    flex: 1,
    textAlign: 'center',
};

const subTitleStyle: React.CSSProperties = {
    textAlign: 'center',
    margin: '20px 0',
    fontSize: '20px',
};

const checkboxContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    marginBottom: '20px',
    fontSize: '18px',
    width: '100%',
};

const textareaStyle: React.CSSProperties = {
    width: '90%', // 增加宽度
    padding: '10px',
    margin: '10px 0',
    fontSize: '16px',
    display: 'none', // Initially hidden
};

const submitButtonStyle: React.CSSProperties = {
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '10px 20px',
    cursor: 'pointer',
    alignSelf: 'center',
    fontSize: '18px',
};

const inputContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    marginBottom: '10px',
};

const inputLabelStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    fontSize: '18px',
    width: '60%', // 增加宽度
    justifyContent: 'flex-end',
    marginRight: '10px',
};

const checkboxLabelStyle: React.CSSProperties = {
    marginLeft: '10px',
    whiteSpace: 'nowrap',
};

export default ReportIssue;
