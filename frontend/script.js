// script.js

document
  .getElementById("uploadForm")
  .addEventListener("submit", async (event) => {
    event.preventDefault();

    const fileInput = document.getElementById("fileInput");
    const patternInput = document.getElementById("patternInput");
    const responseDiv = document.getElementById("response");

    if (!fileInput.files[0] || !patternInput.value) {
      responseDiv.textContent = "Please select a file and enter a pattern.";
      return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);
    formData.append("pattern", patternInput.value);

    responseDiv.textContent = "Uploading...";

    try {
      const response = await fetch("http://localhost:3000/api/file/upload", {
        method: "POST",
        body: formData,
      });

      const result = await response.json();
      if (result.success) {
        // Create the table and add headers
        const table = document.createElement("table");
        const thead = document.createElement("thead");
        const tbody = document.createElement("tbody");

        // Add table headers
        const headerRow = document.createElement("tr");
        const headers = ["Algorithm", "Pattern Found", "Time(ms)"];
        headers.forEach((header) => {
          const th = document.createElement("th");
          th.textContent = header;
          headerRow.appendChild(th);
        });
        thead.appendChild(headerRow);

        // Add data rows
        result.data.forEach((item) => {
          const row = document.createElement("tr");

          const algorithmCell = document.createElement("td");
          algorithmCell.textContent = item.algorithm;

          const patternFoundCell = document.createElement("td");
          patternFoundCell.textContent = item.patternFound ? "True" : "False";
          patternFoundCell.style.color = item.patternFound ? "green" : "red";

          const timeTakenCell = document.createElement("td");
          timeTakenCell.textContent = item.timeTaken;

          row.appendChild(algorithmCell);
          row.appendChild(patternFoundCell);
          row.appendChild(timeTakenCell);

          tbody.appendChild(row);
        });

        table.appendChild(thead);
        table.appendChild(tbody);

        // Clear previous content and append the table to the responseDiv
        responseDiv.innerHTML = ""; // Clear any existing content
        responseDiv.appendChild(table);
      } else {
        responseDiv.textContent = result.msg;
      }
    } catch (error) {
      console.error("Error:", error);
      responseDiv.textContent = "An error occurred. Please try again.";
    }
  });
