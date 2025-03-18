import React, { useEffect, useState } from 'react';
import './DataView.css';

function DataView() {
    const [data, setData] = useState([]);
    const [selectedRow, setSelectedRow] = useState(null);
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [challengeId, setChallengeId] = useState('');

    // Helper function to fetch data
    const fetchData = () => {
        fetch('http://localhost:8080/triathlon/referees')
            .then(response => response.json())
            .then(data => setData(data))
            .catch(error => console.error('Error fetching data:', error));
    };

    // Initial data fetch
    useEffect(() => {
        fetchData();
    }, []);

    const handleAddReferee = () => {
        const newReferee = { name, password, challenge: { id: parseInt(challengeId) } };
        fetch('http://localhost:8080/triathlon/referees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newReferee),
        })
            .then(response => response.json())
            .then(() => {
                fetchData();  // Refetch data after adding
                setName('');
                setPassword('');
                setChallengeId('');
            })
            .catch(error => console.error('Error adding referee:', error));
    };

    const handleUpdateReferee = () => {
        if (selectedRow !== null) {
            const updatedReferee = { ...data[selectedRow], name, password, challenge: { id: parseInt(challengeId) } };
            fetch(`http://localhost:8080/triathlon/referees/${updatedReferee.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedReferee),
            })
                .then(response => response.json())
                .then(() => {
                    fetchData();  // Refetch data after updating
                    setName('');
                    setPassword('');
                    setChallengeId('');
                    setSelectedRow(null);
                })
                .catch(error => console.error('Error updating referee:', error));
        }
    };

    const handleDeleteReferee = () => {
        if (selectedRow !== null) {
            const idToDelete = data[selectedRow].id;
            fetch(`http://localhost:8080/triathlon/referees/${idToDelete}`, {
                method: 'DELETE',
            })
                .then(() => {
                    fetchData();  // Refetch data after deleting
                    setSelectedRow(null);
                })
                .catch(error => console.error('Error deleting referee:', error));
        }
    };

    const handleRowClick = (index) => {
        setSelectedRow(index);
        setName(data[index].name);
        setPassword(data[index].password);
        setChallengeId(data[index].challenge.id);
    };

    return (
        <div className="data-view">
            <h1>Referees</h1>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Password</th>
                    <th>Challenge ID</th>
                </tr>
                </thead>
                <tbody>
                {Array.isArray(data) && data.map((referee, index) => (
                    <tr key={index} onClick={() => handleRowClick(index)}>
                        <td>{referee.name}</td>
                        <td>{'*'.repeat(referee.password.length)}</td>
                        <td>{referee.challenge.id}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div>
                <input
                    type="text"
                    placeholder="Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Challenge ID"
                    value={challengeId}
                    onChange={(e) => setChallengeId(e.target.value)}
                />
                <button onClick={handleAddReferee}>Add</button>
                <button onClick={handleUpdateReferee} disabled={selectedRow === null}>Update</button>
                <button onClick={handleDeleteReferee} disabled={selectedRow === null}>Delete</button>
            </div>
        </div>
    );
}

export default DataView;
