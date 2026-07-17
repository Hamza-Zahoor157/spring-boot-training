const API = "/api/v1/news";
const TOKEN_KEY = "lecture02_access_token";

let allNews = [];

function getAccessToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setAccessToken(token) {
    if (token) {
        localStorage.setItem(TOKEN_KEY, token);
    } else {
        localStorage.removeItem(TOKEN_KEY);
    }
}

function authHeaders(headers = {}) {
    const accessToken = getAccessToken();
    return accessToken
        ? { ...headers, Authorization: `Bearer ${accessToken}` }
        : headers;
}

function getCookie(name) {
    const cookie = document.cookie
        .split("; ")
        .find(entry => entry.startsWith(`${name}=`));

    return cookie ? decodeURIComponent(cookie.split("=").slice(1).join("=")) : null;
}

function csrfHeaders(headers = {}) {
    const csrfToken = getCookie("XSRF-TOKEN");

    return csrfToken
        ? { ...headers, "X-XSRF-TOKEN": csrfToken }
        : headers;
}

async function ensureCsrfCookie() {
    if (getCookie("XSRF-TOKEN")) {
        return;
    }

    // Trigger a same-origin request so Spring Security can emit XSRF-TOKEN.
    await fetch("/", { credentials: "same-origin" });
}

document.addEventListener("DOMContentLoaded", async () => {
    captureTokenFromUrl();
    updateAuthStatus();
    await loadNews();

    document.getElementById("title").addEventListener("input", validateForm);
    document.getElementById("reportedBy").addEventListener("input", validateForm);
    document.getElementById("details").addEventListener("input", validateForm);

    validateForm();
});

function captureTokenFromUrl() {
    const params = new URLSearchParams(window.location.search);

    const token = params.get("token");
    if (token) {
        setAccessToken(token);
        params.delete("token");
        const newUrl = window.location.pathname + (params.toString() ? "?" + params.toString() : "");
        window.history.replaceState({}, document.title, newUrl);
        return;
    }

    const error = params.get("error");
    if (error === "no_local_user") {
        alert("No local user found for Google account: " + (params.get("username") || ""));
        params.delete("error");
        params.delete("username");
        const newUrl = window.location.pathname + (params.toString() ? "?" + params.toString() : "");
        window.history.replaceState({}, document.title, newUrl);
    }
}

function updateAuthStatus() {
    const status = document.getElementById("authStatus");
    if (!status) {
        return;
    }

    status.innerText = getAccessToken() ? "Authenticated" : "Not authenticated";
}

async function login() {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    if (!username || !password) {
        alert("Enter username and password.");
        return;
    }

    await ensureCsrfCookie();

    const response = await fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            ...csrfHeaders()
        },
        body: new URLSearchParams({ username, password })
    });

    if (!response.ok) {
        alert("Login failed.");
        return;
    }

    const payload = await response.json();
    setAccessToken(payload.access_token);
    document.getElementById("reportedBy").value = username;
    updateAuthStatus();
    await loadNews();
}

function logout() {
    setAccessToken(null);
    document.getElementById("reportedBy").value = "";
    updateAuthStatus();
}

async function loadNews() {
    try {
        const response = await fetch(API, {
            headers: authHeaders()
        });

        const responseBody = await response.json();
        allNews = responseBody.content;

        updateStatistics();
        renderNews(allNews);
    } catch (error) {
        console.error(error);
        alert("Unable to load news.");
    }
}

function validateForm() {
    const title = document.getElementById("title").value.trim();
    const reportedBy = document.getElementById("reportedBy").value.trim();
    const details = document.getElementById("details").value.trim();

    const button = document.getElementById("saveButton");
    const valid = title.length > 0 && reportedBy.length > 0 && details.length > 0;

    button.disabled = !valid;

    if (valid) {
        button.classList.remove("bg-gray-300", "text-gray-500", "cursor-not-allowed");
        button.classList.add("bg-black", "text-white", "hover:bg-gray-900");
    } else {
        button.classList.remove("bg-black", "text-white", "hover:bg-gray-900");
        button.classList.add("bg-gray-300", "text-gray-500", "cursor-not-allowed");
    }
}

function renderNews(newsList) {
    const container = document.getElementById("newsContainer");
    const empty = document.getElementById("emptyState");

    container.innerHTML = "";

    if (newsList.length === 0) {
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

function updateStatistics() {
    document.getElementById("totalNews").innerText = String(allNews.length);
    document.getElementById("lastUpdated").innerText = new Date().toLocaleString();
}

function searchNews() {
    const keyword = document.getElementById("search").value.toLowerCase();

    const filtered = allNews.filter(news =>
        news.title.toLowerCase().includes(keyword)
        || news.reportedBy.toLowerCase().includes(keyword)
        || news.details.toLowerCase().includes(keyword)
    );

    renderNews(filtered);
}

async function saveNews() {
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

    const method = id ? "PUT" : "POST";
    const url = id ? `${API}/${id}` : API;

    await fetch(url, {
        method,
        headers: {
            ...authHeaders(),
            "Content-Type": "application/json"
        },
        body: JSON.stringify(news)
    });

    clearForm();
    await loadNews();
}

function editNews(id) {
    const news = allNews.find(n => n.newsId === id);

    document.getElementById("newsId").value = news.newsId;
    document.getElementById("title").value = news.title;
    document.getElementById("reportedBy").value = news.reportedBy;
    document.getElementById("details").value = news.details;

    window.scrollTo({ top: 0, behavior: "smooth" });
    validateForm();
}

async function deleteNews(id) {
    const confirmed = confirm("Delete this news?");

    if (!confirmed) {
        return;
    }

    await fetch(`${API}/${id}`, {
        method: "DELETE",
        headers: authHeaders()
    });

    await loadNews();
}

function clearForm() {
    document.getElementById("newsId").value = "";
    document.getElementById("title").value = "";
    document.getElementById("reportedBy").value = "";
    document.getElementById("details").value = "";
    validateForm();
}
