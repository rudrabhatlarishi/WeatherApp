document.getElementById('getWeather').addEventListener('click', () => {
    const location = document.getElementById('location').value;

    if (location) {
        // Update the URL to your backend endpoint
        fetch(`http://localhost:8080/weather?city=${location}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Location not found'); // Handle non-200 responses
                }
                return response.json(); // Parse response as JSON
            })
            .then(data => {
                // Update the output with weather data
                document.getElementById('output').innerHTML = `
                    <p><strong>City:</strong> ${data.city}</p>
                    <p><strong>Temperature:</strong> ${data.temperature}°C</p>
                    <p><strong>Weather:</strong> ${data.weather}</p>
                `;
            })
            .catch(error => {
                // Display error message
                document.getElementById('output').innerHTML = `<p style="color: red;">${error.message}</p>`;
            });
    } else {
        // Handle empty location input
        document.getElementById('output').innerHTML = `<p style="color: red;">Please enter a location.</p>`;
    }
});
