package com.rushr.Util;


public final class Constants {

    public static final class StatusCodes
    {
        public static final long noContent=204;

        public static final long otpExpired=410;

        public static final long invalidOtp=422;

        public static final long loggedInOtherDevive=103;

        public static final long badRequest=400;

        public static final long unAuthorized=401;

        public static final long notFound=404;

        public static final long methodNotAllowed=405;

        public static final long success=200;

        public static final long error =500;

    }

    public static final class Messages{

        public static final String emptyRequestBody = "Empty Request Body";

        public static final String emptyDeviceType="Empty Device Type";

        public static final String emptyEmployeeId ="Empty Employee Id";

        public static final String emptyFcmToken ="Empty FCM Token";

        public static final String emptyDeviceId="Empty Device Id";

        public static final String loggedOutMessage="User Logged Out";

        public static final String noUserFound="No User Found";

        public static final String noOtpFound="OTP Not Found";

        public static final String alreadyLoggedIn="Already Logged In";

        public static final String otpExpired="OTP Expired";

        public static final String noActiveUserFound="No Active User Found";

        public static final String invalidOTP="Invalid OTP";

        public static final String error = "An unexpected error occurred";

        public static final String success = "Success";

        public static final String invalidPassword="Invalid Password";

        public static final String emptyEmailId="Empty Email Id";

        public static final String emptyList="Empty List";

        public static final String unAuthorized ="UnAuthorized";

        public static final String chapterNotFound ="Chapter Not Found";

        public static final String userMembertOfAnotherChapter = "User already a member of another chapter";

        public static final String userMemberOfThisChapter = "You are already a member";

        public static final String userDontHaveAccessToApprove = "You don't have access to respond this request";

        public static final String userMemberRequestAlreadySent = "You have already sent join request to this Chapter";

    }

}
