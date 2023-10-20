import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.newsmead.fragments.saved.SavedAllFragment
import com.newsmead.fragments.saved.SavedListsFragment

public class TabFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedListsFragment()
            1 -> SavedAllFragment()
            else -> SavedListsFragment()
        }
    }
}