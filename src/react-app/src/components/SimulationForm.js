import React, { useState } from 'react';

const SimulationForm = ({ onSubmit }) => {
    const [formData, setFormData] = useState({
        numDoctors: '',
        serviceTimeDoctor: '',
        numNurses: '',
        serviceTimeNurse: '',
        numAA: '',
        serviceTimeAA: '',
        numRooms: '',
        priorityQueueSize: '',
        arrivalProbabilities: '',
        simIntervalTime: '',
        totalSimTime: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Convert probabilities to an array
        const dataToSubmit = {
            ...formData,
            arrivalProbabilities: formData.arrivalProbabilities.split(',').map(Number)
        };
        onSubmit(dataToSubmit);
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Number of Doctors:
                <input type="number" name="numDoctors" value={formData.numDoctors} onChange={handleChange} required />
            </label>
            <label>
                Doctor Service Time:
                <input type="number" name="serviceTimeDoctor" value={formData.serviceTimeDoctor} onChange={handleChange} required />
            </label>
            <label>
                Number of Nurses:
                <input type="number" name="numNurses" value={formData.numNurses} onChange={handleChange} required />
            </label>
            <label>
                Nurse Service Time:
                <input type="number" name="serviceTimeNurse" value={formData.serviceTimeNurse} onChange={handleChange} required />
            </label>
            <label>
                Number of Admin Assistants:
                <input type="number" name="numAA" value={formData.numAA} onChange={handleChange} required />
            </label>
            <label>
                Admin Assistant Service Time:
                <input type="number" name="serviceTimeAA" value={formData.serviceTimeAA} onChange={handleChange} required />
            </label>
            <label>
                Number of Rooms:
                <input type="number" name="numRooms" value={formData.numRooms} onChange={handleChange} required />
            </label>
            <label>
                Number of Patient Queues:
                <input type="number" name="priorityQueueSize" value={formData.priorityQueueSize} onChange={handleChange} required />
            </label>
            <label>
                Arrival Probabilities (comma-separated):
                <input type="text" name="arrivalProbabilities" value={formData.arrivalProbabilities} onChange={handleChange} required />
            </label>
            <label>
                Simulation Interval Time:
                <input type="number" name="simIntervalTime" value={formData.simIntervalTime} onChange={handleChange} required />
            </label>
            <label>
                Total Simulation Time:
                <input type="number" name="totalSimTime" value={formData.totalSimTime} onChange={handleChange} required />
            </label>
            <button type="submit">Submit</button>
        </form>
    );
};

export default SimulationForm;