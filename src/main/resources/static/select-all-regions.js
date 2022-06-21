function toggle(source) {
    const checkboxes = document.getElementsByName('checkedRegions');
    let i = 0, n = checkboxes.length;
    for(; i<n; i++) {
        checkboxes[i].checked = source.checked;
    }
}
