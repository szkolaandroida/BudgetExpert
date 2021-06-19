package pl.szkoleniaandroid.billexpert.features.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.szkoleniaandroid.billexpert.databinding.AdminFragmentBinding

class AdminFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = AdminFragmentBinding.inflate(inflater, container, false).root

}