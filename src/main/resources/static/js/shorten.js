const form = document.getElementById('shorten-form');
const urlInput = document.getElementById('url-input');
const submitButton = document.getElementById('submit-button');
const resultPanel = document.getElementById('result-panel');
const resultLink = document.getElementById('result-link');
const resultError = document.getElementById('result-error');
const copyButton = document.getElementById('copy-button');

function showError(message) {
    resultPanel.classList.add('hidden');
    resultError.textContent = message;
    resultError.classList.remove('hidden');
}

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    submitButton.disabled = true;
    try {
        const response = await fetch('/shorten', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ url: urlInput.value }),
        });

        if (!response.ok) {
            const message = await response.text();
            showError(message || 'Não foi possível encurtar essa URL.');
            return;
        }

        const data = await response.json();
        const shortUrl = `${window.location.origin}/${data.hashUrl}`;
        resultError.classList.add('hidden');
        resultLink.href = shortUrl;
        resultLink.textContent = shortUrl;
        resultPanel.classList.remove('hidden');
    } catch (error) {
        showError('Não foi possível conectar ao servidor. Tente novamente.');
    } finally {
        submitButton.disabled = false;
    }
});

copyButton.addEventListener('click', async () => {
    try {
        await navigator.clipboard.writeText(resultLink.href);
        copyButton.textContent = 'Copiado!';
    } catch (error) {
        copyButton.textContent = 'Falha ao copiar';
    } finally {
        setTimeout(() => { copyButton.textContent = 'Copiar'; }, 1500);
    }
});