
<!--    checkAll 체크하면 모두 다 선택되게 -->
document.getElementById('checkAll').addEventListener('change', function () {
    var isChecked = this.checked;

    // '.item-checkbox' 클래스를 가진 모든 요소를 선택
    var checkboxes = document.querySelectorAll('.item-checkbox');
    // 각 체크박스에 대해 루프를 돌며 checkAll의 상태에 맞춰 체크 상태를 변경
    checkboxes.forEach(function (checkbox) {
        checkbox.checked = isChecked;
    });

    //합계 재계산 !!
    setTotal();

});

// 모든 .item-checkbox 체크박스에 대해 change 이벤트 리스너 추가
var itemCheckboxes = document.querySelectorAll('.item-checkbox');
itemCheckboxes.forEach(function (checkbox) {
    checkbox.addEventListener('change', function () {
        // .item-checkbox 중 하나라도 체크 해제되면 checkAll 체크박스 해제
        var allChecked = Array.from(itemCheckboxes).every(function (item) {
            return item.checked;
        });

        document.getElementById('checkAll').checked = allChecked;

        //합계 재계산 !!
        setTotal();

    });
});
