const API = "/api/v1/news";

let allNews = [];

document.addEventListener("DOMContentLoaded", async() => {

    await loadNews();

    document
        .getElementById("title")
        .addEventListener("input", validateForm);

    document
        .getElementById("reportedBy")
        .addEventListener("input", validateForm);

    document
        .getElementById("details")
        .addEventListener("input", validateForm);

    validateForm();

});


async function loadNews() {

    try {

        const response = await fetch(API);

        const responseBody = await response.json();

        console.log("Response Body:", responseBody);
        console.log("Content:", responseBody.content);
        console.log("Is Array?", Array.isArray(responseBody.content));

        allNews = responseBody.content;

        updateStatistics();

        renderNews(allNews);

    }

    catch (error) {

        console.error(error);

        alert("Unable to load news.");

    }

}
function validateForm() {

    const title =
        document.getElementById("title").value.trim();

    const reportedBy =
        document.getElementById("reportedBy").value.trim();

    const details =
        document.getElementById("details").value.trim();
    console.log({
        title,
        reportedBy,
        details
    });


    const button =
        document.getElementById("saveButton");



    const valid =
        title.length > 0 &&
        reportedBy.length > 0 &&
        details.length > 0;

    button.disabled = !valid;

    if (valid) {

        button.classList.remove(
            "bg-gray-300",
            "text-gray-500",
            "cursor-not-allowed"
        );

        button.classList.add(
            "bg-black",
            "text-white",
            "hover:bg-gray-900"
        );

    } else {

        button.classList.remove(
            "bg-black",
            "text-white",
            "hover:bg-gray-900"
        );

        button.classList.add(
            "bg-gray-300",
            "text-gray-500",
            "cursor-not-allowed"
        );

    }

}

function renderNews(newsList) {

    const container = document.getElementById("newsContainer");

    const empty = document.getElementById("emptyState");

    container.innerHTML = "";

    if(newsList.length === 0){

        empty.classList.remove("hidden");

        return;

    }

    empty.classList.add("hidden");

    newsList.forEach(news => {

        container.innerHTML += `

        <div class="bg-white rounded-xl border shadow-sm p-6 hover:shadow-md transition">

            <div class="flex justify-between items-start">

                <div>

                    <h2 class="text-xl font-semibold">

                        ${news.title}

                    </h2>

                    <p class="text-gray-600 mt-3">

                        ${news.details}

                    </p>

                </div>

                <span
                    class="text-sm text-gray-400">

                    #${news.newsId}

                </span>

            </div>

            <div class="flex justify-between items-center mt-6">

                <div>

                    <p class="text-sm text-gray-500">

                        Reported By

                    </p>

                    <p class="font-medium">

                        ${news.reportedBy}

                    </p>

                </div>

                <div class="flex gap-3">

                    <button

                        onclick="editNews(${news.newsId})"

                        class="px-4 py-2 rounded-lg border hover:bg-gray-100">

                        Edit

                    </button>

                    <button

                        onclick="deleteNews(${news.newsId})"

                        class="px-4 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700">

                        Delete

                    </button>

                </div>

            </div>

        </div>

        `;

    });

}


function updateStatistics(){

    document.getElementById("totalNews").innerText = String(allNews.length);

    document.getElementById("lastUpdated").innerText =
        new Date().toLocaleString();

}


function searchNews(){

    const keyword =
        document.getElementById("search")
            .value
            .toLowerCase();

    const filtered =
        allNews.filter(news =>

            news.title.toLowerCase().includes(keyword)

            ||

            news.reportedBy.toLowerCase().includes(keyword)

            ||

            news.details.toLowerCase().includes(keyword)

        );

    renderNews(filtered);

}

async function saveNews(){
    const title = document.getElementById("title").value.trim();

    const reportedBy = document.getElementById("reportedBy").value.trim();

    const details = document.getElementById("details").value.trim();

    if (!title) {
        alert("Title is required.");
        return;
    }

    if (!reportedBy) {
        alert("Reported By is required.");
        return;
    }

    if (!details) {
        alert("Details are required.");
        return;
    }

    const id = document.getElementById("newsId").value;

    const news = {
        title,
        reportedBy,
        details,
        reportedAt: new Date().toISOString()
    };


    const method =
        id ? "PUT" : "POST";

    const url =
        id ? `${API}/${id}` : API;

    await fetch(url,{

        method,

        headers:{

            "Content-Type":"application/json"

        },

        body:JSON.stringify(news)

    });

    clearForm();

    await loadNews();

}

function editNews(id){

    const news =
        allNews.find(n => n.newsId === id);

    document.getElementById("newsId").value =
        news.newsId;

    document.getElementById("title").value =
        news.title;

    document.getElementById("reportedBy").value =
        news.reportedBy;

    document.getElementById("details").value =
        news.details;

    window.scrollTo({

        top:0,

        behavior:"smooth"

    });
    validateForm();

}


async function deleteNews(id){

    const confirmed =
        confirm("Delete this news?");

    if(!confirmed){

        return;

    }

    await fetch(`${API}/${id}`,{

        method:"DELETE"

    });

    await loadNews();

}


function clearForm(){

    document.getElementById("newsId").value="";

    document.getElementById("title").value="";

    document.getElementById("reportedBy").value="";

    document.getElementById("details").value="";
    validateForm();

}