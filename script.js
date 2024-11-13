document.getElementById('getWeather').addEventListener('click', () => {
    const location = document.getElementById('location').value;
    if (location) {
        fetch(`http://localhost:8080/weather?city=${location}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Location not found');
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('output').innerHTML = `

                    <h2>Weather for ${data.city}</h2>
                    <p><strong>Temperature:</strong> ${data.temperature}°C</p>
                    <p><strong>Weather:</strong> ${data.weather}</p>
                    <p><strong>Humidity:</strong> ${data.humidity}%</p>
                    <p><strong>Wind Speed:</strong> ${data.windSpeed} m/s</p>

                `;
            })
            .catch(error => {
                document.getElementById('output').innerHTML = `<p style="color: red;">${error.message}</p>`;
            });
    } else {
        document.getElementById('output').innerHTML = `<p style="color: red;">Please enter a location.</p>`;
    }
});
