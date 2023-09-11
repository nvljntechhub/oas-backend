package icbt.oas.backend.service;

import icbt.oas.backend.model.Consultant;
import icbt.oas.backend.model.JobSeeker;
import icbt.oas.backend.model.User;
import icbt.oas.backend.repository.ConsultantRepository;
import icbt.oas.backend.repository.JobSeekerRepository;
import icbt.oas.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConsultantRepository consultantRepository;
    @Autowired
    JobSeekerRepository jobSeekerRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if(user != null){
                return UserDetailsImpl.build(user);
            }

            Consultant consultant = consultantRepository.findByEmail(email).orElse(null);
            if(consultant != null){
                return ConsultantDetailsImpl.build(consultant);
            }

            JobSeeker jobSeeker = jobSeekerRepository.findByEmail(email).orElse(null);
            if(jobSeeker != null){
                return JobSeekerDetailsImpl.build(jobSeeker);
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
