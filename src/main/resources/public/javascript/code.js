// Validate signup form input
function validateForm() {
    if (!document.forms["signupForm"]["name"].value) {
        alert("Name must be filled out");
        return false;
    }
    if (!document.forms["signupForm"]["address"].value) {
        alert("Address must be filled out");
        return false;
    }
}

// Validate admin form input
function validateAdminForm() {
    if (!document.forms["adminForm"]["owner"].value) {
        alert("Owner name must be filled out");
        return false;
    }
}
