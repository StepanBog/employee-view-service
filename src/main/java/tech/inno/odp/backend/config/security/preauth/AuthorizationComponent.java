package tech.inno.odp.backend.config.security.preauth;

import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component(value = "preAuthorization")
public class AuthorizationComponent implements IAuthorizationComponent {

    @Override
    public boolean EmployeePathIdEqualsTokenCheck(@Nonnull final String id) {
//        String tokenEmployeeId = PresentAuthentication.getUser().getEmployeeId().toString();
//        if (!tokenEmployeeId.equals(id)) {
//            throw new ServiceException(
//                    String.format("EmployeePathIdEqualsTokenCheck incorrect. Expected id - %s, actual id - %s", id, tokenEmployeeId),
//                    ErrorCode.EMPLOYEE_NOT_FOUND);
//        }
        return true;
    }

}
