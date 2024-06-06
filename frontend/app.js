document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('uploadButton').addEventListener('click', uploadPriceFile);
    document.getElementById('submitButton').addEventListener('click', submitSupply);
    document.getElementById('generateReportButton').addEventListener('click', generateReport);
});

function uploadPriceFile() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append('file', file);

    fetch('http://localhost:8081/api/prices/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('File uploaded successfully:', data);
    })
    .catch(error => {
        console.error('Error uploading file:', error);
    });
}

function addProduct() {
    const productsContainer = document.getElementById('products-container');
    const productEntry = document.createElement('div');
    productEntry.classList.add('product-entry');
    productEntry.innerHTML = `
        <div class="form-group">
            <label for="product">Product:</label>
            <select class="product">
                <option value="Apple Type 1">Apple Type 1</option>
                <option value="Apple Type 2">Apple Type 2</option>
                <option value="Pear Type 1">Pear Type 1</option>
                <option value="Pear Type 2">Pear Type 2</option>
            </select>
        </div>
        <div class="form-group">
            <label for="quantity">Quantity:</label>
            <input type="number" class="quantity" min="1">
        </div>
    `;
    productsContainer.appendChild(productEntry);
}

function submitSupply() {
    const supplier = document.getElementById('supplier').value;
    const productEntries = document.querySelectorAll('.product-entry');
    const products = [];

    productEntries.forEach(entry => {
        const productType = entry.querySelector('.product').value;
        const quantity = entry.querySelector('.quantity').value;

        products.push({
            productType,
           // productVariety: productType, // Assuming variety is the same as type
            quantity: parseFloat(quantity)
        });
    });

    const supplyData = {
        supplier,
        products
    };

    fetch('http://localhost:8081/api/shipments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(supplyData)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Supply submitted successfully:', data);
    })
    .catch(error => {
        console.error('Error submitting supply:', error);
    });
}

function generateReport() {
    const startDate = document.getElementById('report-start-date').value;
    const endDate = document.getElementById('report-end-date').value;

    fetch(`http://localhost:8081/api/reports?start=${startDate}&end=${endDate}`)
    .then(response => response.json())
    .then(data => {
        const reportBody = document.getElementById('report-body');
        reportBody.innerHTML = '';
        data.forEach(report => {
            let totalCost = 0;
            const productsHtml = report.products.map(product => {
                const cost = product.quantity * product.price;
                totalCost += cost;
                return `<div>${product.productType} (${product.quantity} кг, ₽${product.price.toFixed(2)}, ₽${cost.toFixed(2)})</div>`;
            }).join('');

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${report.id}</td>
                <td>${report.supplier}</td>
                <td>${report.date}</td>
                <td>${productsHtml}</td>
                <td>${totalCost.toFixed(2)}</td>
            `;
            reportBody.appendChild(row);
        });
    })
    .catch(error => {
        console.error('Error generating report:', error);
    });
}

