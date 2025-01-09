document.getElementById('fetch-output').addEventListener('click', () => {
    const outputElement = document.getElementById('simulation-output');
    outputElement.textContent = 'Fetching output...';

    fetch('simulationOut.txt')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            outputElement.textContent = data;
        })
        .catch(error => {
            outputElement.textContent = `Error: ${error.message}`;
        });
});
