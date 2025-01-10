import React from 'react';
import SimulationForm from './components/SimulationForm';

const App = () => {
  const handleFormSubmit = (data) => {
    console.log('Submitted Data:', data);
    // Call API Gateway endpoint here using fetch or axios
    // Example:
    // fetch('https://your-api-gateway-endpoint', {
    //     method: 'POST',
    //     headers: { 'Content-Type': 'application/json' },
    //     body: JSON.stringify(data),
    // })
    //     .then(response => response.json())
    //     .then(result => console.log(result))
    //     .catch(error => console.error('Error:', error));
  };

  return (
      <div>
        <h1>Urgent Care Simulation</h1>
        <SimulationForm onSubmit={handleFormSubmit} />
      </div>
  );
};

export default App;
