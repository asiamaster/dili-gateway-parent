<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
    let _form = $('#_form');
    let _modal = $('#_modal');

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        $(window).resize(function () {
            _grid.bootstrapTable('resetView')
        });
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/route/listPage.action'});
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/
    /**
     * 打开新增窗口
     */
    function openInsertHandler() {
        _modal.modal('show');
        _modal.find('.modal-title').text('新增路由');
    }

    /**
     * 打开修改窗口
     */
    function openUpdateHandler(row) {
        if(!row){
            //获取选中行的数据
            let rows = _grid.bootstrapTable('getSelections');
            if (null == rows || rows.length == 0) {
                bs4pop.alert('请选中一条数据');
                return;
            }
            row = rows[0];
        }
        _modal.modal('show');
        _modal.find('.modal-title').text('修改路由');
        //修改时禁用启用状态
        $("[name='_enabled']").attr("disabled", true);
        //不允许修改路由id
        $("#_routeId").attr("readonly", true);
        let formData = $.extend({}, row);
        formData = bui.util.addKeyStartWith(bui.util.getOriginalData(formData), "_");
        //由于数据库中的原始值是bool类型，而表单控件中只支持字符串
        formData["_enabled"] = formData["_enabled"] +"";
        // alert(JSON.stringify(formData));
        bui.util.loadFormData(formData);
    }

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnableHandler(enable) {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        //table选择模式是单选时可用
        let selectedRow = rows[0];
        let msg = (enable || 'true' == enable) ? '确定要启用该路由吗？' : '确定要禁用该路由吗？';

        bs4pop.confirm(msg, undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/route/doEnable.action",
                    data: {id: selectedRow.id, enable: enable},
                    processData:true,
                    dataType: "json",
                    success : function(data) {
                        bui.loading.hide();
                        if(data.success){
                            _grid.bootstrapTable('refresh');
                            _modal.modal('hide');
                        }else{
                            bs4pop.alert(data.result, {type: 'error'});
                        }
                    },
                    error : function() {
                        bui.loading.hide();
                        bs4pop.alert('远程访问失败', {type: 'error'});
                    }
                });
            }
        })
    }

    /**
     * 删除路由
     */
    function doDeleteHandler(){
//获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        //table选择模式是单选时可用
        let selectedRow = rows[0];
        bs4pop.confirm("确定要删除该路由吗？", undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/route/del.action",
                    data: {routeId: selectedRow.routeId},
                    processData:true,
                    dataType: "json",
                    success : function(data) {
                        bui.loading.hide();
                        if(data.success){
                            _grid.bootstrapTable('refresh');
                            _modal.modal('hide');
                        }else{
                            bs4pop.alert(data.result, {type: 'error'});
                        }
                    },
                    error : function() {
                        bui.loading.hide();
                        bs4pop.alert('远程访问失败', {type: 'error'});
                    }
                });
            }
        })
    }

    function isJSONArrayString(str) {
        if (typeof str == 'string') {
            try {
                var obj=JSON.parse(str);
                if(typeof obj == 'object' && obj && obj.length){
                    return true;
                }else{
                    return false;
                }

            } catch(e) {
                console.log('error：'+str+'!!!'+e);
                return false;
            }
        }
        console.log('It is not a string!')
        return false;
    }

    /**
     *  保存及更新表单数据
     */
    function saveOrUpdateHandler() {
        if (_form.validate().form() != true) {
            return;
        }
        if(!isJSONArrayString($("#_predicates").val().trim())){
            bs4pop.alert("断言格式必须是jsonarray", {type: 'error'});
            return;
        }
        if($("#_filters").val().trim() != "" && !isJSONArrayString($("#_filters").val().trim())){
            bs4pop.alert("过滤器格式必须是jsonarray", {type: 'error'});
            return;
        }
        //修改提交时打开启用状态
        $("[name='_enabled']").attr("disabled", false);
        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = bui.util.removeKeyStartWith(_form.serializeObject(), "_");
        let _url = null;
        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/route/add.action";
        } else {//有id就修改
            _url = "${contextPath}/route/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData: true,
            dataType: "json",
            success: function (data) {
                bui.loading.hide();
                if (data.code == "200") {
                    _grid.bootstrapTable('refresh');
                    _modal.modal('hide');
                } else {
                    bs4pop.alert(data.result, {type: 'error'});
                }
            },
            error: function (a, b, c) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }


    /**
     * 查询
     */
    function queryDataHandler() {
        _grid.bootstrapTable('refresh');
    }

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * 前置 table初始化时 queryParams方法拿不到option对象，需要在页面加载时初始化option
     * @param params
     */
    function queryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        }
        return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    //表单弹框关闭事件
    _modal.on('hidden.bs.modal', function () {
        _form[0].reset();
        //重置表单验证到初始状态
        $(this).find('input,select,textarea').removeClass('is-invalid is-valid');
        $(this).find('input,select,textarea').removeAttr('disabled readonly');
        $(this).find('.invalid-feedback').css('display','none');
        //清空id隐藏框
        $("#_id").val("");
    });

    //行点击事件
    _grid.on('click-row.bs.table', function (e, row, $element, field) {
        var enabled = row.$_enabled;
        if (enabled == ${@com.dili.dr.glossary.EnabledEnum.DISABLED.getCode()}) {
            //当用户状态为 禁用，可操作 启用
            $('#btn_enable').attr('disabled', false);
            $('#btn_disabled').attr('disabled', true);
        } else if (enabled == ${@com.dili.dr.glossary.EnabledEnum.ENABLED.getCode()}) {
            //当用户状态为正常时，则只能操作 禁用
            $('#btn_enable').attr('disabled', true);
            $('#btn_disabled').attr('disabled', false);
        } else {
            //其它情况，按钮不可用
            $('#btn_enable').attr('disabled', true);
            $('#btn_disabled').attr('disabled', true);
        }
    });

    //双击事件
    _grid.on('dbl-click-row.bs.table', function (e, row, $element, field) {
        openUpdateHandler(row);
    });

    //加载完成后绑定tooltip
    _grid.on('load-success.bs.table', function (e, data) {
        $('[data-toggle="tooltip"]').tooltip();
    });


    /*****************************************自定义事件区 end**************************************/
</script>