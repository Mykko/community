/**
 * login.html
 */

function dologin() {
	var accountId = $("#accountid").val();
	var password = $("#accountpwd").val();
	var check = "y";
	if (accountId.length == 0) {
		$("#accountidbox").addClass("has-error");
		check = "n"
	} else {
		$("#accountidbox").removeClass("has-error");
	}
	if (password.length == 0) {
		$("#accountpwdbox").addClass("has-error");
		check = "n"
	} else {
		$("#accountpwdbox").removeClass("has-error");
	}
	if (check == "y") {
		startLogin();
		sendLoginInfo(accountId, password);
		// $.ajax({
		// 	type : "POST",
		// 	dataType : "text",
		// 	url : "homeController/getPublicKey.ajax",
		// 	data : {},
		// 	success : function(result) {
		// 		var publicKeyInfo = eval("(" + result + ")");
		// 		var loginInfo = '{accountId:"' + accountId + '",accountPwd:"'
		// 				+ password + '",time:"' + publicKeyInfo.time + '"}';
		// 		var encrypt = new JSEncrypt();// 加密插件对象
		// 		encrypt.setPublicKey(publicKeyInfo.publicKey);// 设置公钥
		// 		var encrypted = encrypt.encrypt(loginInfo);// 进行加密
		// 		sendLoginInfo(encrypted);
		// 	},
		// 	error : function() {
		// 		showAlert("提示：登录请求失败，请检查网络或服务器运行状态");
		// 	}
		// });
	}
}

function sendLoginInfo(accountId, password) {
	$.ajax({
		type : "POST",
		dataType : "text",
		url : "authorize/doLogin.ajax",
		data : {
			// encrypted : encrypted,
			accountId: accountId,
			password: password
			// vercode : $("#vercode").val()
		},
		success : function(result) {
			$("#alertbox").removeClass("alert");
			$("#alertbox").removeClass("alert-danger");
			$("#alertbox").text("");
			$("#vercodebox").html("");
			$("#vercodebox").removeClass("show");
			$("#vercodebox").addClass("hidden");
			switch (result) {
			case "success":
				finishLogin();
				window.localStorage.setItem("id", accountId);
				$("#accountidbox").removeClass("has-error");
				$("#accountpwdbox").removeClass("has-error");
				window.location.href = "/";
				break;
			case "mustlogout":
				showAlert("提示：您已经登入了一个账户，请先注销后再执行本操作");
				break;
			case "invalidaccount":
				$("#accountidbox").addClass("has-error");
				$("#accountpwdbox").removeClass("has-error");
				showAlert("提示：登录失败，账户不存在或未设置");
				break;
			case "invalidpwd":
				$("#accountpwdbox").addClass("has-error");
				$("#accountidbox").removeClass("has-error");
				showAlert("提示：登录失败，密码错误或未设置");
				break;
			case "needsubmitvercode":
				finishLogin();
				$("#vercodebox").html("<label id='vercodetitle' class='col-sm-6'><img id='showvercode' class='vercodeimg' alt='点击获取验证码' src='homeController/getNewVerCode.do?s="+(new Date()).getTime()+"' onclick='getNewVerCode()'></label><div class='col-sm-6'><input type='text' class='form-control' id='vercode' placeholder='验证码……'></div>");
				$("#vercodebox").removeClass("hidden");
				$("#vercodebox").addClass("show");
				break;
			case "cannotlogin":
				showAlert("提示：登录失败，登录请求无法通过效验（可能是请求耗时过长导致的）");
				break;
			default:
				showAlert("提示：无法登录，未知错误");
				break;
			}
		},
		error : function() {
			showAlert("提示：登录请求失败，请检查网络或服务器运行状态");
		}
	});
}

//获取一个新的验证码
function getNewVerCode(){
	$("#showvercode").attr("src","authorize/getNewVerCode.do?s="+(new Date()).getTime());
}

function showAlert(text){
	finishLogin();
	$("#alertbox").addClass("alert");
	$("#alertbox").addClass("alert-danger");
	$("#alertbox").text(text);
}

function startLogin(){
	$("#loginBtn").attr('disabled','disabled');
	$("#accountid").attr('disabled','disabled');
	$("#accountpwd").attr('disabled','disabled');
	$("#vercode").attr('disabled','disabled');
	$("#loginBtn").val('正在登录...');
}

function finishLogin(){
	$("#loginBtn").removeAttr('disabled');
	$("#accountid").removeAttr('disabled');
	$("#accountpwd").removeAttr('disabled');
	$("#vercode").removeAttr('disabled');
	$("#loginBtn").val('登录');
}