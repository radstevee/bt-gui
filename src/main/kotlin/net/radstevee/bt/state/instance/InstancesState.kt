package net.radstevee.bt.state.instance

public data class InstancesState(
  public val instances: List<Instance> = listOf()
) {
  public fun displayedName(): String {
    return if (instances.isEmpty()) {
      "No Instance Running"
    } else {
      instances.last().version
    }
  }
}
