package com.sequenceiq.cloudbreak.service.hostgroup;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.domain.Recipe;
import com.sequenceiq.cloudbreak.domain.projection.HostGroupRepairView;
import com.sequenceiq.cloudbreak.domain.stack.cluster.host.HostGroup;
import com.sequenceiq.cloudbreak.repository.HostGroupRepository;

@Service
public class HostGroupService {

    @Inject
    private HostGroupRepository hostGroupRepository;

    public Set<HostGroup> getByCluster(Long clusterId) {
        return hostGroupRepository.findHostGroupsInCluster(clusterId);
    }

    public Optional<HostGroup> findHostGroupInClusterByName(Long clusterId, String hostGroupName) {
        return hostGroupRepository.findHostGroupInClusterByNameWithInstanceMetadas(clusterId, hostGroupName);
    }

    public boolean hasHostGroupInCluster(Long clusterId, String hostGroupName) {
        return hostGroupRepository.hasHostGroupInCluster(clusterId, hostGroupName);
    }

    public HostGroup save(HostGroup hostGroup) {
        return hostGroupRepository.save(hostGroup);
    }

    public Optional<HostGroup> getByClusterIdAndName(Long clusterId, String instanceGroupName) {
        return hostGroupRepository.findHostGroupInClusterByNameWithInstanceMetadas(clusterId, instanceGroupName);
    }

    public Optional<HostGroupRepairView> getRepairViewByClusterIdAndName(Long clusterId, String instanceGroupName) {
        return hostGroupRepository.findHostGroupRepairViewInClusterByName(clusterId, instanceGroupName);
    }

    public Set<HostGroup> findHostGroupsInCluster(Long clusterId) {
        return hostGroupRepository.findHostGroupsInCluster(clusterId);
    }

    public void deleteAll(Iterable<HostGroup> hostGroups) {
        hostGroupRepository.deleteAll(hostGroups);
    }

    public Set<HostGroup> findAllHostGroupsByRecipe(Long recipeId) {
        return hostGroupRepository.findAllHostGroupsByRecipe(recipeId);
    }

    public Set<Recipe> getRecipesByHostGroups(Set<HostGroup> hostGroups) {
        return hostGroups.stream().flatMap(hostGroup -> hostGroup.getRecipes().stream()).collect(Collectors.toSet());
    }

    public Set<HostGroup> getByClusterWithRecipes(Long clusterId) {
        return hostGroupRepository.findHostGroupsInClusterWithRecipes(clusterId);
    }

    public HostGroup getByClusterIdAndNameWithRecipes(Long clusterId, String hostGroupName) {
        return hostGroupRepository.findHostGroupInClusterByNameWithRecipes(clusterId, hostGroupName);
    }

}
