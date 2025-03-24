package net.radstevee.bt.view.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.radstevee.bt.state.instance.Instance
import net.radstevee.bt.state.instance.InstancesState

public class InstancesViewModel : ViewModel() {
  private val _instancesState = MutableStateFlow(InstancesState())
  public val instancesState: StateFlow<InstancesState> = _instancesState.asStateFlow()

  public fun add(instance: Instance) {
    _instancesState.update { state ->
      state.copy(
        instances = state.instances + instance
      )
    }
  }

  public fun removeLast() {
    if (_instancesState.value.instances.isEmpty()) {
      return
    }

    _instancesState.update { state ->
      state.copy(
        instances = state.instances - state.instances.last()
      )
    }
  }
}
