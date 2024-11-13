document.getElementById('getWeather').addEventListener('click', () => {
    const location = document.getElementById('location').value;

    if (location) {
        fetch(`/api/weather?location=${location}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Location not found');
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('output').innerHTML = `
                    <p><strong>${data.city}</strong></p>
                    <p>Weather: ${data.weather}</p>
                    <p>Temperature: ${data.temperature}°C</p>
                `;
            })
            .catch(error => {
                document.getElementById('output').innerHTML = `<p style="color: red;">${error.message}</p>`;
            });
    } else {
        document.getElementById('output').innerHTML = `<p style="color: red;">Please enter a location.</p>`;
    }
});
