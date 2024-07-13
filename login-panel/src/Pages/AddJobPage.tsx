// src/Pages/AddJobPage.tsx

import React, { useState } from 'react';
import { AddJobMessage } from 'Plugins/JobAPI/AddJobMessage';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import AdminLayout from './AdminLayout';

const AddJobPage: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleJobSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        const form = e.target as HTMLFormElement;
        const formData = new FormData(form);

        const jobData = {
            jobShortDescription: formData.get('jobShortDescription') as string,
            jobLongDescription: formData.get('jobLongDescription') as string,
            jobHardness: parseInt(formData.get('jobHardness') as string),
            jobCredit: parseInt(formData.get('jobCredit') as string),
            jobRequired: parseInt(formData.get('jobRequired') as string)
        };

        const message = new AddJobMessage(
            jobData.jobShortDescription,
            jobData.jobLongDescription,
            jobData.jobHardness.toString(),
            jobData.jobCredit.toString(),
            jobData.jobRequired.toString()
        );
        console.log(message);

        sendPostRequest(message, setError, () => {
            setSuccess("Job successfully added");
            setTimeout(() => setSuccess(null), 2000);
        });
    };

    return (
        <AdminLayout>
            <h2 style={headingStyle}>Add Job</h2>
            <form onSubmit={handleJobSubmit}>
                <div style={formGroupStyle}>
                    <label htmlFor="jobShortDescription" style={labelStyle}>Short Description:</label>
                    <input type="text" id="jobShortDescription" name="jobShortDescription" required style={inputStyle} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobLongDescription" style={labelStyle}>Long Description:</label>
                    <input type="text" id="jobLongDescription" name="jobLongDescription" required style={inputStyle} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobHardness" style={labelStyle}>Hardness:</label>
                    <input type="number" id="jobHardness" name="jobHardness" required style={inputStyle} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobCredit" style={labelStyle}>Credit:</label>
                    <input type="number" id="jobCredit" name="jobCredit" required style={inputStyle} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobRequired" style={labelStyle}>Required:</label>
                    <input type="number" id="jobRequired" name="jobRequired" required style={inputStyle} />
                </div>
                <button type="submit" style={submitButtonStyle}>Submit Job</button>
            </form>
            {error && <p style={errorMessageStyle}>{error}</p>}
            {success && <SuccessModal message={success} onClose={() => setSuccess(null)} />}
        </AdminLayout>
    );
};

const headingStyle: React.CSSProperties = {
    marginBottom: '20px',
    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif',
    color: '#333',
};

const formGroupStyle: React.CSSProperties = {
    marginBottom: '15px',
};

const labelStyle: React.CSSProperties = {
    display: 'block',
    marginBottom: '5px',
    fontWeight: 'bold',
    color: '#555',
};

const inputStyle: React.CSSProperties = {
    width: '100%',
    padding: '8px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    boxSizing: 'border-box',
};

const submitButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'background 0.3s',
};

const errorMessageStyle: React.CSSProperties = {
    color: 'red',
    fontWeight: 'bold',
};

export default AddJobPage;
